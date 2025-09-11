package org.roxy.crawler.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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
public class RabbitMQCrawlerConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.crawler.power.outage.queue.name}")
    private String powerOutageQueueName;

    @Value("${rabbitmq.crawler.power.outage.routing.key}")
    private String powerOutageRoutingKey;

    @Value("${rabbitmq.crawler.exchange.name}")
    private String crawlerExchangeName;

    private final Integer POWER_OUTAGE_QUEUE_SIZE = 1_000;
    private final Integer POWER_OUTAGE_QUEUE_TTL_MILLISECONDS = 30_000;

    private final Map<String, Object> POWER_OUTAGE_QUEUE_ARGS = new HashMap<>() {{
        put("x-max-length", POWER_OUTAGE_QUEUE_SIZE);
        put("ttl", POWER_OUTAGE_QUEUE_TTL_MILLISECONDS);
    }};


    @Bean
    public DirectExchange crawlerUpdatesMessageExchange() {
        return new DirectExchange(crawlerExchangeName);
    }

    @Bean
    public Queue powerOutageMessageQueue() {
        return new Queue(powerOutageQueueName, true, false, false, POWER_OUTAGE_QUEUE_ARGS);
    }

    @Bean
    public Binding powerOutageMessageBinding() {
        return BindingBuilder.bind(powerOutageMessageQueue())
                .to(crawlerUpdatesMessageExchange())
                .with(powerOutageRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }
}