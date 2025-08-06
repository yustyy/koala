package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.DeviceService;
import com.exskylab.koala.business.abstracts.NotificationService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.constants.NotificationMessages;
import com.exskylab.koala.core.dtos.notification.request.*;
import com.exskylab.koala.core.exceptions.*;
import com.exskylab.koala.core.producers.EmailTaskDto;
import com.exskylab.koala.core.producers.NotificationProducerService;
import com.exskylab.koala.core.producers.PushTaskDto;
import com.exskylab.koala.core.producers.SmsTaskDto;
import com.exskylab.koala.dataAccess.NotificationDao;
import com.exskylab.koala.entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationManager implements NotificationService {

    private final NotificationDao notificationDao;
    private final NotificationProducerService notificationProducerService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(NotificationManager.class);
    private final ObjectMapper objectMapper;
    private final DeviceService deviceService;

    public NotificationManager(NotificationDao notificationDao,
                               @Lazy NotificationProducerService notificationProducerService,
                               @Lazy UserService userService, ObjectMapper objectMapper, DeviceService deviceService) {
        this.notificationDao = notificationDao;
        this.notificationProducerService = notificationProducerService;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.deviceService = deviceService;
    }

    @Override
    @Transactional
    public Notification sendEmail(SendEmailDto sendEmailDto, DispatchPriority dispatchPriority, boolean isSystemMail) {
        logger.info("Sending email notification to userId: {} destinationEmail: {}", sendEmailDto.getRecipientId(), sendEmailDto.getDestinationEmail());

        User sender;
        if (isSystemMail) {
            sender = userService.getSystemUser();
        } else {
            sender = userService.getAuthenticatedUser();
        }

        User recipient = null;
        if (sendEmailDto.getRecipientId() != null) {
            recipient = userService.getById(sendEmailDto.getRecipientId());
        }

        String finalTemplateName;
        Map<String, Object> finalTemplateParameters;
        String finalSubject;

        if (sendEmailDto.getTemplateName() != null) {
            finalTemplateName = sendEmailDto.getTemplateName();
            finalTemplateParameters = sendEmailDto.getTemplateParameters();
            finalSubject = sendEmailDto.getSubject();
        } else if (sendEmailDto.getHtmlBody() != null && sendEmailDto.getSubject() != null) {
            finalTemplateName = "raw-html-template";
            finalTemplateParameters = Map.of("htmlContent", sendEmailDto.getHtmlBody());
            finalSubject = sendEmailDto.getSubject();
        } else {
            logger.error("Email notification could not be queued for userId: {}. Cause: {}",
                    sendEmailDto.getRecipientId(), NotificationMessages.TEMPLATE_OR_SUBJECT_NOT_PROVIDED);
            throw new NotificationDispatchException(NotificationMessages.TEMPLATE_OR_SUBJECT_NOT_PROVIDED);
        }

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setCategory(sendEmailDto.getCategory());
        notification.setDestinationEmail(sendEmailDto.getDestinationEmail());
        notification.setSubject(finalSubject);
        notification.setStatus(NotificationStatus.QUEUED);

        try {
            notification.setTemplateParameters(objectMapper.writeValueAsString(finalTemplateParameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while serializing template parameters for email with userId: {} ErrorMessage: {}", sendEmailDto.getRecipientId(), e.getMessage());
            throw new TemplateParametersSerializationException("{parameters.could.not.serialize}");
        }

        var savedNotification = notificationDao.save(notification);

        EmailTaskDto taskDto = new EmailTaskDto(
                savedNotification.getId(),
                savedNotification.getDestinationEmail(),
                finalSubject,
                finalTemplateName,
                finalTemplateParameters
        );

        executeDispatch(
                () -> notificationProducerService.sendEmailTask(taskDto),
                NotificationChannel.EMAIL,
                dispatchPriority,
                savedNotification.getId()
        );

        return savedNotification;
    }

    @Override
    @Transactional
    public Notification sendSms(SendSmsDto sendSmsDto, DispatchPriority dispatchPriority, boolean isSystemSms) {
        logger.info("Sending SMS notification to userId: {} destinationPhoneNumber: {}", sendSmsDto.getRecipientId(), sendSmsDto.getDestinationPhoneNumber());

        User sender;
        if (isSystemSms) {
            sender = userService.getSystemUser();
        } else {
            sender = userService.getAuthenticatedUser();
        }

        User recipient = null;
        if (sendSmsDto.getRecipientId() != null) {
            recipient = userService.getById(sendSmsDto.getRecipientId());
        }

        String finalTemplateName;
        Map<String, Object> finalTemplateParameters;

        if (sendSmsDto.getTemplateName() != null) {
            finalTemplateName = sendSmsDto.getTemplateName();
            finalTemplateParameters = sendSmsDto.getTemplateParameters();
        } else if (sendSmsDto.getMessageBody() != null) {
            finalTemplateName = "raw-sms-template";
            finalTemplateParameters = Map.of("textContent", sendSmsDto.getMessageBody());
        } else {
            logger.error("SMS notification could not be queued for userId: {}. Cause: {}",
                    sendSmsDto.getRecipientId(), NotificationMessages.TEMPLATE_OR_MESSAGE_BODY_NOT_PROVIDED);
            throw new NotificationDispatchException(NotificationMessages.TEMPLATE_OR_MESSAGE_BODY_NOT_PROVIDED);
        }


        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setChannel(NotificationChannel.SMS);
        notification.setCategory(sendSmsDto.getCategory());
        notification.setDestinationPhoneNumber(sendSmsDto.getDestinationPhoneNumber());
        notification.setStatus(NotificationStatus.QUEUED);

        try {
            notification.setTemplateParameters(objectMapper.writeValueAsString(finalTemplateParameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while serializing template parameters for email with userId: {} ErrorMessage: {}", sendSmsDto.getRecipientId(), e.getMessage());
            throw new TemplateParametersSerializationException("{parameters.could.not.serialize}");
        }

        var savedNotification = notificationDao.save(notification);
        SmsTaskDto taskDto = new SmsTaskDto(
                savedNotification.getId(),
                savedNotification.getDestinationPhoneNumber(),
                finalTemplateName,
                finalTemplateParameters
        );

        executeDispatch(
                () -> notificationProducerService.sendSmsTask(taskDto),
                NotificationChannel.SMS,
                dispatchPriority,
                savedNotification.getId()
        );

        return savedNotification;

    }

    @Override
    @Transactional
    public void sendPushToUser(SendPushToUserDto sendPushToUserDto, DispatchPriority dispatchPriority, boolean isSystemPush) {
        logger.info("Dispatching push notification to userId: {} with title: {}",
                sendPushToUserDto.getRecipientId(), sendPushToUserDto.getTitle());

        if (sendPushToUserDto.getRecipientId() == null) {
            throw new ArgumentCannotBeNullException(NotificationMessages.USER_ID_CANNOT_BE_NULL);
        }

        User sender = isSystemPush ? userService.getSystemUser(): userService.getAuthenticatedUser();
        User recipient = userService.getById(sendPushToUserDto.getRecipientId());

        List<Device> usersDevices = deviceService.getDevicesByUserIdAndPushTokenNotNull(sendPushToUserDto.getRecipientId());
        if (usersDevices.isEmpty()) {
            logger.warn("No devices found for userId: {}. Push notification wont be sent.", sendPushToUserDto.getRecipientId());
            return;
        }

        String finalTitle;
        String finalBody;
        Map<String, String> finalDataPayload;
        String finalTemplateName;
        Map<String, Object> finalTemplateParameters;

        if (sendPushToUserDto.getTemplateName()!= null){
            finalTemplateName = sendPushToUserDto.getTemplateName();
            finalTemplateParameters = sendPushToUserDto.getTemplateParameters();
            finalTitle = sendPushToUserDto.getTitle();
            finalBody = sendPushToUserDto.getBody();
            finalDataPayload = sendPushToUserDto.getDataPayload();
        } else if (sendPushToUserDto.getTitle() != null && sendPushToUserDto.getBody() != null) {
            finalTemplateName = "raw-push-template";
            finalTemplateParameters = Map.of("title", sendPushToUserDto.getTitle(), "body", sendPushToUserDto.getBody());
            finalTitle = sendPushToUserDto.getTitle();
            finalBody = sendPushToUserDto.getBody();
            finalDataPayload = sendPushToUserDto.getDataPayload() != null ? sendPushToUserDto.getDataPayload() : Map.of();
        }else {
            logger.error("PushDTO is invalid. provide either template or direct content.");
            throw new NotificationDispatchException(NotificationMessages.TEMPLATE_OR_CONTENT_NOT_PROVIDED);
        }

        for (Device device : usersDevices) {
            Notification notification = new Notification();
            notification.setRecipient(recipient);
            notification.setSender(sender);
            notification.setChannel(NotificationChannel.PUSH);
            notification.setCategory(sendPushToUserDto.getCategory());
            notification.setDestinationPushToken(device.getPushToken());
            notification.setStatus(NotificationStatus.QUEUED);
            notification.setSubject(finalTitle);
            notification.setTemplateParameters(finalTemplateName);

            try{
                notification.setTemplateParameters(objectMapper.writeValueAsString(finalTemplateParameters));
            }catch (JsonProcessingException e){
                logger.error("Error while serializing template parameters for push notification with userId: {} ErrorMessage: {}", sendPushToUserDto.getRecipientId(), e.getMessage());
                throw new TemplateParametersSerializationException("{parameters.could.not.serialize}");
            }

            var savedNotification = notificationDao.save(notification);

            PushTaskDto taskDto = new PushTaskDto(
                    savedNotification.getId(),
                    savedNotification.getDestinationPushToken(),
                    finalTitle,
                    finalBody,
                    finalDataPayload
            );


            executeDispatch(
                    () -> notificationProducerService.sendPushTask(taskDto),
                    NotificationChannel.PUSH,
                    dispatchPriority,
                    savedNotification.getId()
            );
        }

    }

    @Override
    public Notification sendPushToDevice(SendPushToDeviceDto sendPushToDeviceDto, DispatchPriority dispatchPriority, boolean isSystemPush) {
        return null; //TODO: Implement this method
    }

    @Override
    public void sendBulkPush(SendBulkPushDto sendBulkPushDto, DispatchPriority dispatchPriority, boolean isSystemPush) {
            //TODO: Implement this method
    }


    private void executeDispatch(Runnable dispatchAction, NotificationChannel channel, DispatchPriority dispatchPriority,
                                 UUID id) {
        try {
            dispatchAction.run();
        } catch (TaskCannotBeSentException e) {
            logger.warn("Dispatch to queue failed for notification id: {}, Priority: {}. Error: {}"
                    , id, dispatchPriority, e.getMessage());


            if (dispatchPriority == DispatchPriority.CRITICAL) {
                switch (channel) {
                    case EMAIL -> {
                        logger.error("Critical email notification could not be queued for notification id: {}. " +
                                "The process will stop.", id);
                        throw new NotificationDispatchException("{critical.email.dispatch.failed}");
                    }
                    case SMS -> {
                        logger.error("Critical SMS notification could not be queued for notification id: {}. " +
                                "The process will stop.", id);
                        throw new NotificationDispatchException("{critical.sms.dispatch.failed}");
                    }
                    case PUSH -> {
                        logger.error("Critical push notification could not be queued for notification id: {}. " +
                                "The process will stop.", id);
                        throw new NotificationDispatchException("{critical.push.dispatch.failed}");
                    }
                    default -> throw new InvalidCategoryException("{invalid.notification.channel}");
                }

            }

            logger.error("Non-critical notification could not be queued for notification id: {}. " +
                    "The process will continue. A cleanup job might handle this.", id);

        }
    }

/*
    private Locale determineLocale(String requestedLanguage, User recipient){
        if (requestedLanguage != null && !requestedLanguage.isBlank()){
            logger.debug("Users requested language for notification: {}", requestedLanguage);
            return Locale.forLanguageTag(requestedLanguage);
        }

        logger.debug("No language specified for notification. Using default language for user: {}", recipient.get());
        return null;
    }

 */



}
