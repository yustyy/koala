package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.auth.request.AuthStartRegistrationDto;
import com.exskylab.koala.entities.PendingRegistration;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface PendingRegistrationService {
    PendingRegistration getByEmail(String email);

    void deleteById(UUID id);

    PendingRegistration createPendingRegistration(AuthStartRegistrationDto authStartRegistrationDto);

    PendingRegistration getByToken(UUID registrationToken);

    PendingRegistration getById(UUID pendingRegistrationId);
}
