package org.roxy.reminder.bot.service.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.common.dto.PowerOutageDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PowerOutageMessageConsumer {

    private final ObjectMapper objectMapper;
    private final PowerOutageSourceMessageRepository messageRepository;
    private final PowerOutageMessageMapper messageMapper;

    public PowerOutageMessageConsumer(ObjectMapper objectMapper,
                                      PowerOutageSourceMessageRepository messageRepository,
                                      PowerOutageMessageMapper messageMapper
    ) {
        this.objectMapper = objectMapper;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @SneakyThrows
    @RabbitListener(queues = "${rabbitmq.crawler.power.outage.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(Message message, Channel channel) {
        try {
            var powerOutage = objectMapper.readValue(message.getBody(), PowerOutageDto.class);
            messageRepository.save(messageMapper.mapDtoToEntity(powerOutage));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            log.warn("PowerOutageMessage already exists {}",
                    objectMapper.readValue(message.getBody(), PowerOutageDto.class).toString());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("Batch processing failed: {}", e.getMessage());
        }
    }
}