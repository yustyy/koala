package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.User;
import com.exskylab.koala.entities.Verification;
import com.exskylab.koala.entities.VerificationStatus;
import com.exskylab.koala.entities.VerificationType;

import java.util.List;
import java.util.UUID;

public interface VerificationService {


    Verification createVerification(UUID userId, VerificationType verificationType);

    boolean verifyToken(String token);

    boolean isVerified(User user, VerificationType verificationType);

    boolean isValid(String token);

    Verification getVerificationByToken(String token);

    boolean useVerificationToken(String token);

    List<Verification> getVerificationsByUserIdAndType(UUID userId, VerificationType verificationType);

    boolean invalidateVerification(UUID id);
}
