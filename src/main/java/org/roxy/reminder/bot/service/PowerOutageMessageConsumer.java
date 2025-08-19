package org.roxy.reminder.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.repository.PowerOutageNotificationRepository;
import org.roxy.reminder.common.dto.PowerOutageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PowerOutageMessageConsumer {

    private final ObjectMapper objectMapper;
    private final PowerOutageNotificationRepository repository;
    private final PowerOutageMessageMapper mapper;

    public PowerOutageMessageConsumer(ObjectMapper objectMapper, PowerOutageNotificationRepository repository, PowerOutageMessageMapper mapper) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.mapper = mapper;
    }

    @SneakyThrows
    @RabbitListener(queues = "${rabbitmq.crawler.power.outage.queue.name}")
    //TODO сделать batch
    public void handleMessage(String message, Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Received PowerOutageMessage: {}", message);
            var powerOutageInfo = objectMapper.readValue(message, PowerOutageDto.class);
            // FIXME
            // repository.save(mapper.mapDtoToEntity(powerOutageInfo));
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("RabbitMQ message processor failed. Could not process update {}", e.getMessage());
            channel.basicReject(deliveryTag, false);
        }
    }
}
