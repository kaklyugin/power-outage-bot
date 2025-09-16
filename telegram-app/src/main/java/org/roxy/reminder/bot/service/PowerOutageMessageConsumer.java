package org.roxy.reminder.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.service.notification.NotificationService;
import org.roxy.reminder.common.dto.PowerOutageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PowerOutageMessageConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final PowerOutageSourceMessageRepository messageRepository;
    private final PowerOutageMessageMapper messageMapper;

    public PowerOutageMessageConsumer(ObjectMapper objectMapper,
                                      NotificationService notificationService, PowerOutageSourceMessageRepository messageRepository, PowerOutageMessageMapper messageMapper
    ) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @SneakyThrows
    @RabbitListener(queues = "${rabbitmq.crawler.power.outage.queue.name}", containerFactory = "rabbitBatchListenerContainerFactory")
    public void handleMessage(List<Message> messages, Channel channel) {
        try {
            log.info("Received PowerOutageMessage batch count: {}", messages.size());
            List<PowerOutageDto> powerOutageDtos = new ArrayList<>();
            List<Long> deliveryTags = new ArrayList<>();
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                long deliveryTag =  message.getMessageProperties().getDeliveryTag();
                deliveryTags.add(deliveryTag);
                try {
                    PowerOutageDto powerOutage = objectMapper.readValue(message.getBody(),PowerOutageDto.class);
                    powerOutageDtos.add(powerOutage);
                } catch (Exception e) {
                    log.error("Failed to process message {}: {}", i, e.getMessage());
                    channel.basicNack(deliveryTag, false, false);
                }
            }
            var savedMessageCount = messageRepository.saveAll(messageMapper.mapDtoToEntity(powerOutageDtos));
            for (Long deliveryTag : deliveryTags) {
                channel.basicAck(deliveryTag, false);
            }
            log.info("Successfully processed PowerOutageMessage batch count: {}", savedMessageCount);
        } catch (Exception e) {
            log.error("Batch processing failed: {}", e.getMessage());
        }
    }
}