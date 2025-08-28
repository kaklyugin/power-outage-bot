package org.roxy.reminder.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.service.notification.NotificationService;
import org.roxy.reminder.common.dto.PowerOutageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PowerOutageMessageConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public PowerOutageMessageConsumer(ObjectMapper objectMapper,
                                      NotificationService notificationService
                                     ) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @SneakyThrows
    @RabbitListener(queues = "${rabbitmq.crawler.power.outage.queue.name}")
    //TODO сделать batch
    public void handleMessage(String message, Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Received PowerOutageMessage: {}", message);
            var powerOutageInfo = objectMapper.readValue(message, PowerOutageDto.class);
            //FIXME переделать на список после батчей
            notificationService.createNotifications(List.of(powerOutageInfo));
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("RabbitMQ message processor failed. Could not process update {}", e.getMessage());
            channel.basicReject(deliveryTag, false);
        }
    }
}
