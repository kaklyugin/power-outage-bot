package org.roxy.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@EnableScheduling
@EnableAsync
@Slf4j
public class CrawlerSchedulerService {

    private final PowerOutageCrawlService powerOutageTimeTableLoaderService;
    private final MessagePublishService brokerService;

    public CrawlerSchedulerService(PowerOutageCrawlService powerOutageTimeTableLoaderService, ApplicationEventPublisher applicationEventPublisher, MessagePublishService brokerService) {
        this.powerOutageTimeTableLoaderService = powerOutageTimeTableLoaderService;
        this.brokerService = brokerService;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    private void crawl() {
        log.info("Scheduled start of crawling data from Donenergo");
        powerOutageTimeTableLoaderService.crawlPowerOutageTimetable();
        brokerService.publishNewPowerOutageMessages();
    }
}