package com.example.tournaments.cfg;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "tournaments-exchange";

    public static final String RK_TOURNAMENT_CREATED = "tournament.created";
    public static final String RK_TOURNAMENT_UPDATED = "tournament.updated";
    public static final String RK_PLAYER_REGISTERED = "player.registered";
    public static final String RK_TOURNAMENT_RESULTS = "tournament.results";
    public static final String RK_PLAYER_CANCELLED = "player.cancelled";
    public static final String RK_TOURNAMENT_DELETED = "tournament.deleted";
    public static final String RK_TOURNAMENT_DRAW = "tournament.draw";
    public static final String FANOUT_EXCHANGE = "tournaments.events.fanout";
    @Bean
    public TopicExchange tournamentsExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter);
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (!ack) {
//                System.err.println("NACK: Delivery failed! Cause: " + cause);
//            }
//        });
//        return rabbitTemplate;
//    }

}