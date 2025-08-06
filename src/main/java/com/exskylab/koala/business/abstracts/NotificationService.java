package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.notification.request.*;
import com.exskylab.koala.entities.DispatchPriority;
import com.exskylab.koala.entities.Notification;

public interface NotificationService {

    Notification sendEmail(SendEmailDto sendEmailDto, DispatchPriority dispatchPriority, boolean isSystemMail);

    Notification sendSms(SendSmsDto sendSmsDto, DispatchPriority dispatchPriority, boolean isSystemSms);

    void sendPushToUser(SendPushToUserDto sendPushToUserDto, DispatchPriority dispatchPriority, boolean isSystemPush);

    Notification sendPushToDevice(SendPushToDeviceDto sendPushToDeviceDto, DispatchPriority dispatchPriority, boolean isSystemPush);

    void sendBulkPush(SendBulkPushDto sendBulkPushDto, DispatchPriority dispatchPriority, boolean isSystemPush);

}
