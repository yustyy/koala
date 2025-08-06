package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.Device;
import com.exskylab.koala.entities.User;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    List<Device> getDevicesByUserIdAndPushTokenNotNull(UUID id);

    Device findOrCreateDevice(User user, String ipAddress, String userAgent);
}
