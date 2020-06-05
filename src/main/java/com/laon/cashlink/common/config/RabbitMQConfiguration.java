package com.laon.cashlink.common.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class RabbitMQConfiguration {
//
//    private final String exchange = "direct.exchange";
//    private final String RemittanceDLRequestRoutingKey = "remittance.dl.request.route";
//    private final String RemittanceDLCompleteRoutingKey = "remittance.dl.complete.route";
//
//    private static final String REMIT_DL_REQUEST_QUEUE = "remittance.dl.request.queue";
//    private static final String REMIT_DL_COMPLETE_QUEUE = "remittance.dl.complete.queue";
//
//    @Bean
//    public List<Declarable> bindings() {
//        Queue remitDLRequestQueue = new Queue(REMIT_DL_REQUEST_QUEUE, true);
//        Queue remitDLCompleteQueue = new Queue(REMIT_DL_COMPLETE_QUEUE, true);
//
//        DirectExchange directExchange = new DirectExchange(exchange);
//
//        return Arrays.asList(remitDLRequestQueue, remitDLCompleteQueue, directExchange,
//                BindingBuilder.bind(remitDLRequestQueue).to(directExchange).with(RemittanceDLRequestRoutingKey),
//                BindingBuilder.bind(remitDLCompleteQueue).to(directExchange).with(RemittanceDLCompleteRoutingKey));
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter);
//
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//}
