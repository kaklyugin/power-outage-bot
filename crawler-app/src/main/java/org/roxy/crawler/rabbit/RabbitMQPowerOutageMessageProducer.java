package org.roxy.crawler.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.crawler.persistence.repository.PowerOutageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQPowerOutageMessageProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQCrawlerConfig rabbitMQCrawlerConfig;

    public RabbitMQPowerOutageMessageProducer(RabbitTemplate rabbitTemplate, RabbitMQCrawlerConfig rabbitMQCrawlerConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQCrawlerConfig = rabbitMQCrawlerConfig;
    }

    public void sendPowerOutageMessage(PowerOutageEntity powerOutage) {
        log.info("Sending new power outage message: {}", powerOutage.toString());
        rabbitTemplate.convertAndSend(
                rabbitMQCrawlerConfig.getCrawlerExchangeName(),
                rabbitMQCrawlerConfig.getPowerOutageRoutingKey(),
                powerOutage);
    }
}