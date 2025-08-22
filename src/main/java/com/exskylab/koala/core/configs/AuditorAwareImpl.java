package com.exskylab.koala.core.configs;

import com.exskylab.koala.business.abstracts.SecurityService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorAwareImpl implements AuditorAware<User> {

    private final SecurityService securityService;

    public AuditorAwareImpl(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        try{
            User authenticatedUser = securityService.getAuthenticatedUser();
            return Optional.of(authenticatedUser);
        } catch (Exception e){
            return Optional.of(securityService.getSystemUser());
        }
    }


    @Bean
    public AuditorAware<User> auditorAware(){
        return new AuditorAwareImpl(securityService);
    }
}
