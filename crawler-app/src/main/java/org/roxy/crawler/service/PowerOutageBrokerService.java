package org.roxy.crawler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.crawler.persistence.repository.PowerOutageRepository;
import org.roxy.crawler.rabbit.RabbitMQCrawlerConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class PowerOutageBrokerService {

    private final ObjectMapper objectMapper;
    private final PowerOutageRepository powerOutageRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQCrawlerConfig rabbitMQCrawlerConfig;

    public PowerOutageBrokerService( ObjectMapper objectMapper, PowerOutageRepository powerOutageRepository, RabbitTemplate rabbitTemplate, RabbitMQCrawlerConfig rabbitMQCrawlerConfig) {
        this.objectMapper = objectMapper;
        this.powerOutageRepository = powerOutageRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQCrawlerConfig = rabbitMQCrawlerConfig;
    }

    private void sendMessage(PowerOutageEntity powerOutage) {
        log.info("Sending new power outage message: {}", powerOutage.toString());
        //TODO send as DTO
        rabbitTemplate.convertAndSend(
                rabbitMQCrawlerConfig.getCrawlerExchangeName(),
                rabbitMQCrawlerConfig.getPowerOutageRoutingKey(),
                powerOutage);
    }

    public void publishNewPowerOutageMessages() {
        try {
            List<PowerOutageEntity> unsentMessages = powerOutageRepository.findByQueueSentAtIsNull();
            for (PowerOutageEntity item : unsentMessages) {
                 sendMessage(item);
                powerOutageRepository.markAsSent(item.getId());
            }
            log.info("Send {} power-outage message to RabbitMQ ", objectMapper.writeValueAsString(unsentMessages.size()));
        } catch (Exception e) {
            log.error("Error sending power-outage message to RabbitMQ", e);
        }
    }
}