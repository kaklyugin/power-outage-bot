package org.roxy.crawler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.crawler.persistence.repository.PowerOutageRepository;
import org.roxy.crawler.rabbit.RabbitMQPowerOutageMessageProducer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PowerOutageBrokerService {

    private final RabbitMQPowerOutageMessageProducer producer;
    private final ObjectMapper objectMapper;
    private final PowerOutageRepository powerOutageRepository;

    public PowerOutageBrokerService(RabbitMQPowerOutageMessageProducer producer, ObjectMapper objectMapper, PowerOutageRepository powerOutageRepository) {
        this.producer = producer;
        this.objectMapper = objectMapper;
        this.powerOutageRepository = powerOutageRepository;
    }

    @SneakyThrows
    public void sendPowerOutageMessage(List<PowerOutageEntity> powerOutages) {
        for(PowerOutageEntity item: powerOutages) {
            producer.sendPowerOutageMessage(item);
            powerOutageRepository.updateQueueSentAtTime(item.getId());
            log.info("Send power-outage message to RabbitMQ {}", objectMapper.writeValueAsString(item));
        }
    }
}
