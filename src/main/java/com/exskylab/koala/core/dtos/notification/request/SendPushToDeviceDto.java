package com.exskylab.koala.core.dtos.notification.request;

import com.exskylab.koala.entities.NotificationCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendPushToDeviceDto {


    private String targetPushToken;

    private NotificationCategory category;

    private String templateName;
    private Map<String, Object> templateParameters;
    private String title;
    private String body;
    private Map<String, String> dataPayload;


}
