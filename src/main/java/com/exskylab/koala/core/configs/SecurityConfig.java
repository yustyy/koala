package com.exskylab.koala.core.configs;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.security.SessionAuthFilter;
import com.exskylab.koala.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SessionAuthFilter sessionAuthFilter;

    private final SecurityExceptionConfig securityExceptionConfig;

    public SecurityConfig(SessionAuthFilter sessionAuthFilter,
                          SecurityExceptionConfig securityExceptionConfig) {
        this.sessionAuthFilter = sessionAuthFilter;
        this.securityExceptionConfig = securityExceptionConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityExceptionConfig)
                        .accessDeniedHandler(securityExceptionConfig))

                .authorizeHttpRequests(auth -> auth
                        //auth endpoints
                        .requestMatchers("/api/auth/startRegistration").permitAll()
                        .requestMatchers("/api/auth/verifyRegistrationToken").permitAll()
                        .requestMatchers("/api/auth/completeRegistration").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refreshToken").authenticated()
                        .requestMatchers("/api/auth/logout").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(sessionAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
