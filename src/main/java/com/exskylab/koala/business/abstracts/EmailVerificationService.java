package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.EmailVerification;
import com.exskylab.koala.entities.User;

public interface EmailVerificationService {
    EmailVerification startEmailVerification(String newEmail, User user);

}
