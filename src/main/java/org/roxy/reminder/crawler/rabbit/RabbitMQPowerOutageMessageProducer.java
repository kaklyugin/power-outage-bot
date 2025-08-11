package org.roxy.reminder.crawler.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQPowerOutageMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.crawler.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.crawler.power.outage.routing.key}")
    private String routingKey;

    public void sendPowerOutageMessage(String message) {
        System.out.println("Sending new power outage message: " + message);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
