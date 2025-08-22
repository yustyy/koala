package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.userVerification.request.VerificationRequestDto;
import com.exskylab.koala.entities.UserVerification;

import java.util.UUID;

public interface UserVerificationService {
    UserVerification verify(UUID verificationId, VerificationRequestDto verificationRequestDto);

}
