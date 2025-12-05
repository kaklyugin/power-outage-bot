package org.roxy.crawler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.crawler.persistence.repository.PowerOutageRepository;
import org.roxy.crawler.rabbitconfig.RabbitMQCrawlerConfig;
import org.roxy.dto.PowerOutageMessageDto;
import org.roxy.mapper.PowerOutageMessageMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class MessagePublishService {

    private final ObjectMapper objectMapper;
    private final PowerOutageRepository powerOutageRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQCrawlerConfig rabbitMQCrawlerConfig;
    private final PowerOutageMessageMapper mapper;

    public MessagePublishService(ObjectMapper objectMapper, PowerOutageRepository powerOutageRepository, RabbitTemplate rabbitTemplate, RabbitMQCrawlerConfig rabbitMQCrawlerConfig, PowerOutageMessageMapper mapper) {
        this.objectMapper = objectMapper;
        this.powerOutageRepository = powerOutageRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQCrawlerConfig = rabbitMQCrawlerConfig;
        this.mapper = mapper;
    }

    private void sendMessage(PowerOutageMessageDto powerOutage) {
        log.info("Sending new power outage message: {}", powerOutage.toString());
        rabbitTemplate.convertAndSend(
                rabbitMQCrawlerConfig.getCrawlerExchangeName(),
                rabbitMQCrawlerConfig.getPowerOutageRoutingKey(),
                powerOutage);
    }

    public void publishNewPowerOutageMessages() {
        try {
            List<PowerOutageEntity> unsentMessages = powerOutageRepository.findNotSentMessages();
            for (PowerOutageEntity item : unsentMessages) {
                PowerOutageMessageDto message = mapper.mapPowerOutageEntityToDto(item);
                 sendMessage(message);
                powerOutageRepository.markAsSent(item.getId());
            }
        } catch (Exception e) {
            log.error("Error sending power-outage message to RabbitMQ", e);
        }
    }
}