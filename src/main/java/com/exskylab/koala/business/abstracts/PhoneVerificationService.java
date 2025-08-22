package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.PhoneVerification;
import com.exskylab.koala.entities.User;

public interface PhoneVerificationService {

    PhoneVerification startPhoneVerification(String newPhoneNumber, User user);

}
