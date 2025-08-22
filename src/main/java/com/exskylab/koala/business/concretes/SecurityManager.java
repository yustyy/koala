package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.SecurityService;
import com.exskylab.koala.core.exceptions.UserNotFoundException;
import com.exskylab.koala.dataAccess.UserDao;
import com.exskylab.koala.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SecurityManager implements SecurityService {

    private final UserDao userDao;

    private final Logger logger = LoggerFactory.getLogger(SecurityManager.class);

    public SecurityManager(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getAuthenticatedUser() {
        logger.info("Getting authenticated user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            logger.warn("User not authenticated or anonymous");
            throw new UserNotFoundException("{user.not.authenticated}");
        }
        User user = (User) authentication.getPrincipal();
        user = userDao.findById(user.getId()).orElseThrow(() ->
                new UserNotFoundException("{user.not.found}"));
        logger.info("Authenticated user found: {}", user.getId());
        return user;
    }

    @Override
    public User getSystemUser() {
        UUID systemUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        return userDao.findById(systemUserId).orElseThrow( () ->
                new UserNotFoundException("{system.user.not.found}"));
    }
}
