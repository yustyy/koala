package com.exskylab.koala.core.dtos.notification.request;

import com.exskylab.koala.entities.NotificationCategory;
import com.exskylab.koala.entities.PushAudienceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendBulkPushDto {


    private PushAudienceType audienceType;

    private NotificationCategory category;

    private String templateName;
    private Map<String, Object> templateParameters;
    private String title;
    private String body;
    private Map<String, String> dataPayload;

}
