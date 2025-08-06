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
public class SendEmailDto {

    private UUID recipientId;
    private NotificationCategory category;
    private String destinationEmail;

    //for templates
    private String templateName;
    private Map<String, Object> templateParameters;

    //for custom
    private String subject;
    private String htmlBody;

}
