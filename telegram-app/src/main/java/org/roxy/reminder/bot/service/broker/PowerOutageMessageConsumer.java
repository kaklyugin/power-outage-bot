package org.roxy.reminder.bot.service.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import org.roxy.reminder.bot.service.OutageMessageService;
import org.roxy.reminder.bot.service.broker.dto.PowerOutageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class PowerOutageMessageConsumer {

    private final ObjectMapper objectMapper;
    private final OutageMessageService outageMessageService;
    public PowerOutageMessageConsumer(ObjectMapper objectMapper,
                                      OutageMessageService outageMessageService
    ) {
        this.objectMapper = objectMapper;
        this.outageMessageService = outageMessageService;
    }

    @RabbitListener(queues = "${rabbitmq.crawler.power.outage.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(Message message, Channel channel) throws IOException {
        try {
            var powerOutage = objectMapper.readValue(message.getBody(), PowerOutageDto.class);
            outageMessageService.saveMessage(powerOutage);
            if (channel.isOpen()) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
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