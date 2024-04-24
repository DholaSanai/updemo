package com.backend.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${queue.name}")
    private String queueName;

    @Value("${room.queue.name}")
    private String roomQueueName;

    @Value("${room.exchange.name}")
    private String roomExchangeName;

    @Value("${exchange.name}")
    private String exchangeName;

    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public Queue testQueue(){
        return new Queue(queueName,true);
    }

    @Bean
    public Queue roomQueue(){return new Queue(roomQueueName,true);}

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(exchangeName);
    }

    @Bean
    Binding testBinding(Queue testQueue, DirectExchange exchange){
        return BindingBuilder.bind(testQueue).to(exchange).with("routing-key-test");
    }

    @Bean
    DirectExchange roomExchange(){ return new DirectExchange(roomExchangeName);}

    @Bean
    Binding roomBinding(Queue roomQueue, DirectExchange roomExchange){
        return BindingBuilder.bind(roomQueue).to(roomExchange).with("routing-key-test");
    }

}
