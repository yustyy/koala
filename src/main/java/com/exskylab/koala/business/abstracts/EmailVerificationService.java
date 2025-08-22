package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.EmailVerification;
import com.exskylab.koala.entities.User;
import jakarta.validation.constraints.Email;

public interface EmailVerificationService {
    EmailVerification startEmailVerification(String newEmail, User user);

}
