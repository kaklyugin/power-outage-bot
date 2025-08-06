package org.roxy.reminder.crawler;

import org.roxy.reminder.crawler.dto.PowerOutageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@EnableAsync
public class CrawlerScheduler {

    private static final Logger log = LoggerFactory.getLogger(CrawlerScheduler.class);
    private final DonEnergoHttpClient httpClient;
    private final DonEnergoHtmlParser htmlParser;

    @Value("${power.outage.urls}")
    private String[] powerOutageUrls;

    public CrawlerScheduler(DonEnergoHttpClient httpClient, DonEnergoHtmlParser htmlParser) {
        this.httpClient = httpClient;
        this.htmlParser = htmlParser;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    private void crawl() throws ExecutionException, InterruptedException {
        CompletableFuture<List<PowerOutageItem>> future1 =
                httpClient.getPageContentAsync(URI.create(powerOutageUrls[0]))
                        .thenApplyAsync(DonEnergoHtmlParser::parsePage);
        List<PowerOutageItem> result = future1.get();
        log.info("Crawling " + result);
    }
}
