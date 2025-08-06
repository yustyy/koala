package com.exskylab.koala.core.dtos.notification.request;

import com.exskylab.koala.entities.NotificationCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendSmsDto {

    private UUID recipientId;
    private NotificationCategory category;
    private String destinationPhoneNumber;

    private String templateName;
    private Map<String, Object> templateParameters;

    private String messageBody;
}
