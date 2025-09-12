package org.roxy.reminder.bot.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RabbitMQConfig {

    private final int BATCH_SIZE = 20;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.updates.exchange.name}")
    private String updatesExchangeName;

    @Value("${rabbitmq.updates.queue.name}")
    private String updatesQueueName;

    @Value("${rabbitmq.updates.routing.key}")
    private String updatesRoutingKey;

    @Bean
    public DirectExchange updatesMessageExchange() {
        return new DirectExchange(updatesExchangeName);
    }

    @Bean
    public Queue updatesMessageQueue() {
        return new Queue(updatesQueueName);
    }

    @Bean
    public Binding updatesMessageBinding() {
        return BindingBuilder.bind(updatesMessageQueue())
                .to(updatesMessageExchange())
                .with(updatesRoutingKey);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitBatchListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConsumerBatchEnabled(true);
        factory.setBatchListener(true);
        factory.setBatchSize(BATCH_SIZE);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }
}
