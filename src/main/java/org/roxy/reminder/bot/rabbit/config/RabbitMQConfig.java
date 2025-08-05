package org.roxy.reminder.bot.rabbit.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RabbitMQConfig {

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
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // Set manual acknowledgment
        return factory;
    }

}
