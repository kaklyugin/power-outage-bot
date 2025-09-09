package org.roxy.crawler.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.DonEnergoHtmlParser;
import org.roxy.crawler.DonEnergoHttpClient;
import org.roxy.crawler.dto.PowerOutageItem;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.crawler.persistence.entity.PowerOutageSourceEntity;
import org.roxy.crawler.persistence.repository.PowerOutageRepository;
import org.roxy.crawler.persistence.repository.PowerOutageSourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PowerOutageTimetableLoader {

    private final DonEnergoHttpClient donEnergoHttpClient;
    private final PowerOutageRepository powerOutageRepository;
    private final PowerOutageBrokerService powerOutageBrokerService;
    private final PowerOutageSourceRepository powerOutageSourceRepository;


    public PowerOutageTimetableLoader(DonEnergoHttpClient donEnergoHttpClient, PowerOutageRepository powerOutageRepository, PowerOutageBrokerService powerOutageBrokerService, PowerOutageSourceRepository powerOutageSourceRepository) {
        this.donEnergoHttpClient = donEnergoHttpClient;
        this.powerOutageRepository = powerOutageRepository;
        this.powerOutageBrokerService = powerOutageBrokerService;
        this.powerOutageSourceRepository = powerOutageSourceRepository;
    }


    public void loadPowerOutageTimetable() {
        List<PowerOutageSourceEntity> powerOutageSourceEntities = powerOutageSourceRepository.findAll();
        for (PowerOutageSourceEntity entity : powerOutageSourceEntities) {
            try {
                log.info("Requesting page {}", entity.getPageUrl());
                List<PowerOutageItem> powerOutageItems = donEnergoHttpClient.getPageContentAsync(URI.create(entity.getPageUrl()))
                        .thenApplyAsync(DonEnergoHtmlParser::parsePage)
                        .get();
                log.info("Page {} is parsed", entity.getPageUrl());
                List<PowerOutageEntity> newPowerOutages = savePowerOutageTimetable(powerOutageItems);
                log.info("New Power Outage Timetable items count: {}", newPowerOutages.size());
                powerOutageBrokerService.sendPowerOutageMessage(newPowerOutages);
                Thread.sleep(3000);
            } catch (Exception e) {
                log.error("Failed to get or parse page {}",  entity.getPageUrl(), e);
            }
        }
    }

    private List<PowerOutageEntity> savePowerOutageTimetable(List<PowerOutageItem> items) {
        List<PowerOutageEntity> entitiesToSave = new ArrayList<>();
        List<PowerOutageEntity> existingEntities = powerOutageRepository.findAll(); //TODO Ограничить
        for (PowerOutageItem item : items) {
            {
                boolean newItemAlreadyExists = existingEntities.stream().
                        anyMatch(p -> p.getHashCode().equals(item.getHashCode()));
                if (!newItemAlreadyExists) {
                    PowerOutageEntity entity = new PowerOutageEntity();
                    entity.setCity(item.getCity());
                    entity.setAddress(item.getAddress());
                    entity.setDateTimeOff(item.getDateTimeOff());
                    entity.setDateTimeOn(item.getDateTimeOn());
                    entity.setPowerOutageReason(item.getPowerOutageReason());
                    entity.setHashCode(item.getHashCode());
                    entitiesToSave.add(entity);
                }
            }
        }
        return powerOutageRepository.saveAll(entitiesToSave);
    }

}
