package org.roxy.reminder.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.service.notification.MessageNotificationService;
import org.roxy.reminder.common.dto.PowerOutageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PowerOutageMessageConsumer {

    private final ObjectMapper objectMapper;
    private final MessageNotificationService messageNotificationService;

    public PowerOutageMessageConsumer(ObjectMapper objectMapper,
                                      MessageNotificationService messageNotificationService
                                     ) {
        this.objectMapper = objectMapper;
        this.messageNotificationService = messageNotificationService;
    }

    @SneakyThrows
    @RabbitListener(queues = "${rabbitmq.crawler.power.outage.queue.name}", containerFactory = "rabbitBatchListenerContainerFactory")
    //TODO сделать batch
    public void handleMessage(List<Message> messages, Channel channel) {
        try {
            log.info("Received PowerOutageMessage batch count: {}", messages.size());

            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                long deliveryTag = message.getMessageProperties().getDeliveryTag();
                try {
                    PowerOutageDto powerOutage = objectMapper.readValue(
                            message.getBody(),
                            PowerOutageDto.class
                    );
                    log.info("Processing message {}: {}", i, powerOutage);
                    //FIXME REWRITE TO SAVE

                    messageNotificationService.createNotifications(List.of(powerOutage));

                    // Acknowledge each message individually
                    channel.basicAck(deliveryTag, false);

                } catch (Exception e) {
                    log.error("Failed to process message {}: {}", i, e.getMessage());
                    // Reject individual message without requeue
                    channel.basicNack(deliveryTag, false, false);
                }
            }
        } catch (Exception e) {
            log.error("Batch processing failed: {}", e.getMessage());
        }
    }
}
