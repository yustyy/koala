package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.session.response.CreatedSessionInfo;
import com.exskylab.koala.entities.Device;
import com.exskylab.koala.entities.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionService {
    CreatedSessionInfo createSession(Device device, String ipAddress);

    Session findActiveSessionById(UUID sessionId);

    Session save(Session session);

    public void invalidateActiveSessionsForDevice(UUID deviceId);

    void invalidateActiveSessionsForUserIdExcludingSessionId(UUID userId, UUID sessionId);
}
