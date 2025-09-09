package org.roxy.crawler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.crawler.rabbit.RabbitMQPowerOutageMessageProducer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PowerOutageBrokerService {

    private final RabbitMQPowerOutageMessageProducer producer;
    private final ObjectMapper objectMapper;

    public PowerOutageBrokerService(RabbitMQPowerOutageMessageProducer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void sendPowerOutageMessage(List<PowerOutageEntity> powerOutages) {
        for(PowerOutageEntity item: powerOutages) {
            String json = objectMapper.writeValueAsString(item);
            producer.sendPowerOutageMessage(json);
            log.info("Send power-outage message to RabbitMQ {}", json);
        }
    }
}
