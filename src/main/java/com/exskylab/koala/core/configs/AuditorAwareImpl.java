package com.exskylab.koala.core.configs;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.entities.User;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {

    private final UserService userService;

    public AuditorAwareImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        try{
            User authenticatedUser = userService.getAuthenticatedUser();
            return Optional.of(authenticatedUser);
        } catch (Exception e){
            return Optional.of(userService.getSystemUser());
        }

    }
}
