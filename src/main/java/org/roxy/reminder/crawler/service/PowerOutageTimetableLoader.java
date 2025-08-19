package org.roxy.reminder.crawler.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.crawler.DonEnergoHtmlParser;
import org.roxy.reminder.crawler.DonEnergoHttpClient;
import org.roxy.reminder.crawler.dto.PowerOutageItem;
import org.roxy.reminder.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.reminder.crawler.persistence.repository.PowerOutageRepository;
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
    @Value("${power.outage.urls}")
    private String[] powerOutageUrls;
    private final PowerOutageBrokerService powerOutageBrokerService;


    public PowerOutageTimetableLoader(DonEnergoHttpClient donEnergoHttpClient, PowerOutageRepository powerOutageRepository, PowerOutageBrokerService powerOutageBrokerService) {
        this.donEnergoHttpClient = donEnergoHttpClient;
        this.powerOutageRepository = powerOutageRepository;
        this.powerOutageBrokerService = powerOutageBrokerService;
    }

    @SneakyThrows
    public void loadPowerOutageTimetable() {
        CompletableFuture<List<PowerOutageItem>> getPowerOutageItemsFuture =
                donEnergoHttpClient.getPageContentAsync(URI.create(powerOutageUrls[0])) //Todo заменить на итерацию по списку
                        .thenApplyAsync(DonEnergoHtmlParser::parsePage);
        List<PowerOutageItem> powerOutageItems = getPowerOutageItemsFuture.get();
        List<PowerOutageEntity> newPowerOutages = savePowerOutageTimetable(powerOutageItems);
        log.info("New Power Outage Timetable items count: {}", newPowerOutages.size());
        powerOutageBrokerService.sendPowerOutageMessage(newPowerOutages);
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
                    entity.setLocation(item.getLocation());
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
