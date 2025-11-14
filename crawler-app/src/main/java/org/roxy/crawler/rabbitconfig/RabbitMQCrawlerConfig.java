package org.roxy.crawler.rabbitconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
@Slf4j
public class RabbitMQCrawlerConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.queue.power-outage.name}")
    private String powerOutageQueueName;

    @Value("${rabbitmq.routing-key.power-outage}")
    private String powerOutageRoutingKey;

    @Value("${rabbitmq.exchange.crawler.name}")
    private String crawlerExchangeName;

    private final Integer POWER_OUTAGE_QUEUE_SIZE = 5_000;
    private final Integer POWER_OUTAGE_QUEUE_TTL_MILLISECONDS = 30_000;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.setIgnoreDeclarationExceptions(false);
        return admin;
    }

    @Bean
    public Declarables crawlerDeclarables() {
        log.info("Creating RabbitMQ resources at startup: exchange={}, queue={}, routingKey={}",
                crawlerExchangeName, powerOutageQueueName, powerOutageRoutingKey);
        DirectExchange exchange = new DirectExchange(crawlerExchangeName, true, false);

        Map<String, Object> queueArgs = new HashMap<>();
        queueArgs.put("x-max-length", POWER_OUTAGE_QUEUE_SIZE);
        queueArgs.put("x-message-ttl", POWER_OUTAGE_QUEUE_TTL_MILLISECONDS);

        Queue queue = new Queue(powerOutageQueueName, true, false, false, queueArgs);

        Binding binding = BindingBuilder.bind(queue)
                .to(exchange)
                .with(powerOutageRoutingKey);

        return new Declarables(exchange, queue, binding);
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }
}