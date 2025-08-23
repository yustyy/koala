package com.exskylab.koala.core.configs;

import com.exskylab.koala.business.abstracts.SecurityService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorAwareImpl implements AuditorAware<User> {

    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuditorAwareImpl(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        try{
            User authenticatedUser = securityService.getAuthenticatedUserFromContext();
            return Optional.of(authenticatedUser);
        } catch (Exception e){
            logger.info("No authenticated user found, using system user for cretatedBy or updatedBy. Exception: {}", e.getMessage());
            return Optional.of(securityService.getSystemUser());
        }
    }


    @Bean
    public AuditorAware<User> auditorAware(){
        return new AuditorAwareImpl(securityService);
    }
}
