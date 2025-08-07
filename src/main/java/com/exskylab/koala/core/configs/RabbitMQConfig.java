package com.exskylab.koala.core.configs;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_ROUTING_KEY = "notification.email";

    public static final String SMS_QUEUE = "sms.queue";
    public static final String SMS_ROUTING_KEY = "notification.sms";

    public static final String PUSH_QUEUE = "push.queue";
    public static final String PUSH_ROUTING_KEY = "notification.push";



    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue emailQueue(){
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    @Bean
    public Queue smsQueue(){
        return QueueBuilder.durable(SMS_QUEUE).build();
    }

    @Bean
    public Queue pushQueue(){
        return QueueBuilder.durable(PUSH_QUEUE).build();
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder
                .bind(emailQueue())
                .to(notificationsExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding smsBinding(){
        return BindingBuilder
                .bind(smsQueue())
                .to(notificationsExchange())
                .with(SMS_ROUTING_KEY);
    }

    @Bean
    public Binding pushBinding(){
        return BindingBuilder
                .bind(pushQueue())
                .to(notificationsExchange())
                .with(PUSH_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setChannelTransacted(true);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }


}
