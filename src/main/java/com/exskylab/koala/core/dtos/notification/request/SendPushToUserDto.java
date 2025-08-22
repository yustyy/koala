package com.exskylab.koala.core.dtos.notification.request;

import com.exskylab.koala.entities.NotificationCategory;
import com.exskylab.koala.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendPushToUserDto {

    private User recipient;

    private NotificationCategory category;

    private String templateName;
    private Map<String, Object> templateParameters;

    private String title;
    private String body;
    private Map<String, String> dataPayload;

    private String language;
}
