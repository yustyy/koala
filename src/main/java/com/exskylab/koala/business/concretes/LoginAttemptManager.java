package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.LoginAttemptService;
import com.exskylab.koala.dataAccess.LoginAttempDao;
import com.exskylab.koala.entities.LoginAttempt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptManager implements LoginAttemptService {

    private final LoginAttempDao loginAttempDao;

    private final Logger logger = LoggerFactory.getLogger(LoginAttemptManager.class);


    public LoginAttemptManager(LoginAttempDao loginAttempDao) {
        this.loginAttempDao = loginAttempDao;
    }

    @Override
    public LoginAttempt save(LoginAttempt attempt) {
        var result = loginAttempDao.save(attempt);
        logger.info("Saved login attempt with id: {}", result.getId());
        return result;
    }
}
