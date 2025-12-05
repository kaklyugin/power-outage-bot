package org.roxy.crawler;

import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.service.PowerOutageBrokerService;
import org.roxy.crawler.service.PowerOutageTimetableLoader;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@EnableScheduling
@EnableAsync
@Slf4j
public class CrawlerScheduler {

    private final PowerOutageTimetableLoader powerOutageTimeTableLoader;
    private final PowerOutageBrokerService brokerService;

    public CrawlerScheduler(PowerOutageTimetableLoader powerOutageTimeTableLoader, ApplicationEventPublisher applicationEventPublisher, PowerOutageBrokerService brokerService) {
        this.powerOutageTimeTableLoader = powerOutageTimeTableLoader;
        this.brokerService = brokerService;
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    private void crawl() {
        log.info("Scheduled start of crawling data from Donenergo");
        powerOutageTimeTableLoader.loadPowerOutageTimetable();
        brokerService.publishNewPowerOutageMessages();
    }
}