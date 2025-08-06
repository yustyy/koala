package com.exskylab.koala.core.configs;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
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


    @Bean
    public AuditorAware<User> auditorAware(){
        return new AuditorAwareImpl(userService);
    }
}
