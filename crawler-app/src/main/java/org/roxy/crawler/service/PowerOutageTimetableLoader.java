package org.roxy.crawler.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.DonEnergoHtmlParser;
import org.roxy.crawler.DonEnergoHttpClient;
import org.roxy.crawler.dto.PowerOutageParsedItem;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.crawler.persistence.entity.PowerOutagePageUrlEntity;
import org.roxy.crawler.persistence.repository.PowerOutageRepository;
import org.roxy.crawler.persistence.repository.PowerOutageSourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PowerOutageTimetableLoader {

    private final DonEnergoHttpClient donEnergoHttpClient;
    private final PowerOutageRepository powerOutageRepository;
    private final PowerOutageBrokerService powerOutageBrokerService;
    private final PowerOutageSourceRepository powerOutagePageRepository;

    @Value("${feature.send-all-saved-items}")
    private boolean FEATURE_TOGGLE_SEND_ALL_SAVED_ITEMS;

    public PowerOutageTimetableLoader(DonEnergoHttpClient donEnergoHttpClient, PowerOutageRepository powerOutageRepository, PowerOutageBrokerService powerOutageBrokerService, PowerOutageSourceRepository powerOutagePageRepository) {
        this.donEnergoHttpClient = donEnergoHttpClient;
        this.powerOutageRepository = powerOutageRepository;
        this.powerOutageBrokerService = powerOutageBrokerService;
        this.powerOutagePageRepository = powerOutagePageRepository;
    }

    @Transactional
    public void loadPowerOutageTimetable() {
        int SLEEP_BETWEEN_REQUESTS_MS = 2_000;

        List<PowerOutagePageUrlEntity> powerOutagePageUrlEntities = powerOutagePageRepository.findAllWhereEnabledIsTrue();
        for (PowerOutagePageUrlEntity powerOutagePageUrl : powerOutagePageUrlEntities) {
            try {
                final String PAGE_URL = powerOutagePageUrl.getPageUrl();
                log.info("Requesting page {}", PAGE_URL);
                List<PowerOutageParsedItem> powerOutageParsedItems = donEnergoHttpClient.getPageContentAsync(URI.create(PAGE_URL))
                        .thenApplyAsync(DonEnergoHtmlParser::parsePage)
                        .get();
                log.info("Page {} is parsed", PAGE_URL);
                List<PowerOutageEntity> newPowerOutages = saveNewPowerOutageParsedItems(powerOutageParsedItems, PAGE_URL);
                if (FEATURE_TOGGLE_SEND_ALL_SAVED_ITEMS) {
                    newPowerOutages = powerOutageRepository.findAll();
                }
                powerOutageBrokerService.sendPowerOutageMessage(newPowerOutages);
                Thread.sleep(SLEEP_BETWEEN_REQUESTS_MS);
            } catch (Exception e) {
                log.error("Failed to get or parse page {}", powerOutagePageUrl.getPageUrl(), e);
            }
        }
    }

    private List<PowerOutageEntity> saveNewPowerOutageParsedItems(List<PowerOutageParsedItem> items, String url) {
        List<PowerOutageEntity> entitiesToSave = new ArrayList<>();
        List<PowerOutageEntity> existingEntities = powerOutageRepository.findAll();
        for (PowerOutageParsedItem item : items) {
            {
                boolean isNew = existingEntities.stream().
                        noneMatch(p -> p.getMessageHashCode().equals(item.getMessageHashCode()));
                if (!isNew) {
                    PowerOutageEntity entity = PowerOutageEntity.builder()
                            .city(item.getCity())
                            .address(item.getAddress())
                            .dateTimeOff(item.getDateTimeOff())
                            .dateTimeOn(item.getDateTimeOn())
                            .powerOutageReason(item.getPowerOutageReason())
                            .url(url)
                            .messageHashCode(item.getMessageHashCode())
                            .comment(item.getComment())
                            .build();
                    entitiesToSave.add(entity);
                }
            }
        }
        return powerOutageRepository.saveAll(entitiesToSave);
    }
}