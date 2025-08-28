package org.roxy.reminder.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.updates.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.updates.routing.key}")
    private String routingKey;

    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
