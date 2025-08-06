package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.session.response.CreatedSessionInfo;
import com.exskylab.koala.entities.Device;
import com.exskylab.koala.entities.Session;

public interface SessionService {
    CreatedSessionInfo createSession(Device device, String ipAddress);
}
