package org.roxy.reminder.bot.service.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.roxy.reminder.bot.events.PowerOutageMessageEvent;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.service.OutageMessageService;
import org.roxy.reminder.bot.service.broker.dto.PowerOutageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class PowerOutageMessageConsumer {

    private final ObjectMapper objectMapper;
    private final OutageMessageService outageMessageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PowerOutageMessageConsumer(ObjectMapper objectMapper,
                                      OutageMessageService outageMessageService, ApplicationEventPublisher applicationEventPublisher
    ) {
        this.objectMapper = objectMapper;
        this.outageMessageService = outageMessageService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RabbitListener(queues = "${rabbitmq.crawler.power.outage.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(Message message, Channel channel) throws IOException {
        try {
            var powerOutage = objectMapper.readValue(message.getBody(), PowerOutageDto.class);
            PowerOutageSourceMessageEntity messageEntity = outageMessageService.saveMessage(powerOutage);
            if (channel.isOpen()) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
            PowerOutageMessageEvent event = new PowerOutageMessageEvent(this, messageEntity);
            applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("Message processing failed: {}", e.getMessage());
            if (channel.isOpen()) {
                try {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                } catch (AlreadyClosedException ex) {
                    log.warn("Channel already closed when trying to nack message");
                }
            }
        }
    }

}