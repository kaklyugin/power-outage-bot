package org.roxy.reminder.crawler;

import org.roxy.reminder.crawler.service.PowerOutageTimetableLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@EnableScheduling
@EnableAsync
public class CrawlerScheduler {

    private final Logger log = LoggerFactory.getLogger(CrawlerScheduler.class);
    private final PowerOutageTimetableLoader powerOutageTimeTableLoader;

    public CrawlerScheduler(PowerOutageTimetableLoader powerOutageTimeTableLoader) {
        this.powerOutageTimeTableLoader = powerOutageTimeTableLoader;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    private void crawl() throws ExecutionException, InterruptedException {
        log.info("Started crawling data from Don Energo");
        powerOutageTimeTableLoader.loadPowerOutageTimetable();
    }
}
