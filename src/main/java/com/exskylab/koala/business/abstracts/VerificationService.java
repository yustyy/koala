package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.User;
import com.exskylab.koala.entities.Verification;
import com.exskylab.koala.entities.VerificationType;

public interface VerificationService {


    Verification createVerification(User user, VerificationType verificationType);

    boolean verifyToken(String token);

    boolean isVerified(User user, VerificationType verificationType);

    boolean isValid(String token);

    Verification getVerificationByToken(String token);

    boolean useVerificationToken(String token);


}
