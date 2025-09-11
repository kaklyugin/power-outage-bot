package org.roxy.reminder.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.sate.machine.UpdateHandlerService;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateMessageConsumer {

    private final ObjectMapper objectMapper;
    private final UpdateHandlerService updateHandler;
    private final ConnectionFactory connectionFactory;

    public UpdateMessageConsumer(ObjectMapper objectMapper, UpdateHandlerService updateHandler, ConnectionFactory connectionFactory) {
        this.objectMapper = objectMapper;
        this.updateHandler = updateHandler;
        this.connectionFactory = connectionFactory;
    }

    @SneakyThrows
    @RabbitListener(queues = "${rabbitmq.updates.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(String message, Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            var update = objectMapper.readValue(message, UpdateDto.class);
            updateHandler.handle(update);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("RabbitMQ message processor failed. Could not process update {}", e.getMessage());
            channel.basicReject(deliveryTag, false);
        }
    }
}
