package com.exskylab.koala.core.producers;

import com.exskylab.koala.core.configs.RabbitMQConfig;
import com.exskylab.koala.core.constants.NotificationMessages;
import com.exskylab.koala.core.exceptions.TaskCannotBeSentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducerService {

    private final RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(NotificationProducerService.class);

    public NotificationProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailTask(EmailTaskDto emailTaskDto) {
        logger.info("Sending email task to queue for id: {}", emailTaskDto.id());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.EMAIL_ROUTING_KEY,
                    emailTaskDto
            );
            logger.info("Email task sent to queue for id: {}", emailTaskDto.id());
        } catch (Exception e) {
            logger.error("Email task could not be sent to queue for id: {}, Error: {}",
                    emailTaskDto.id(),
                    e.getMessage());
            throw new TaskCannotBeSentException(NotificationMessages.EMAIL_TASK_CANNOT_BE_SENT_TO_QUEUE);

        }
    }

    public void sendSmsTask(SmsTaskDto smsTaskDto) {
        logger.info("Sending sms task to queue for id: {}", smsTaskDto.id());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.SMS_ROUTING_KEY,
                    smsTaskDto
            );
            logger.info("Sms task sent to queue for id: {}", smsTaskDto.id());
        } catch (Exception e) {
            logger.error("Sms task could not be sent to queue for id: {}, Error: {}",
                    smsTaskDto.id(),
                    e.getMessage());

            throw new TaskCannotBeSentException(NotificationMessages.SMS_TASK_CANNOT_BE_SENT_TO_QUEUE);
        }
    }

    public void sendPushTask(PushTaskDto pushTaskDto) {
        logger.info("Sending push task to queue for id: {}", pushTaskDto.id());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.PUSH_ROUTING_KEY,
                    pushTaskDto
            );
            logger.info("Push task sent to queue for id: {}", pushTaskDto.id());
        } catch (Exception e) {
            logger.error("Push task could not be sent to queue for id: {}, Error: {}",
                    pushTaskDto.id(),
                    e.getMessage());

            throw new TaskCannotBeSentException(NotificationMessages.PUSH_TASK_CANNOT_BE_SENT_TO_QUEUE);
        }
    }

}
