package com.exskylab.koala.core.producers.notification;


import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public record EmailTaskDto(
        UUID id,
        String to,
        String subject,
        String templateName,
        Map<String, Object> templateParameters
) implements Serializable {}
