package com.exskylab.koala.core.producers;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public record PushTaskDto(
        UUID id,
        String pushToken,
        String title,
        String body,
        Map<String, String> dataPayload
) implements Serializable {}
