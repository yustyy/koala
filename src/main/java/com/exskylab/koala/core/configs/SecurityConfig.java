package com.exskylab.koala.core.configs;

import com.exskylab.koala.core.security.SessionAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

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
                        .requestMatchers(HttpMethod.POST, "/auth/start-registration").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/verify-registration-token").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/complete-registration").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/refresh-token").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/logout").authenticated()

                        //companycontactinvitation endpoints
                        .requestMatchers(HttpMethod.POST,"/company-contact-invitations/{invitationId}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/company-contact-invitations/{invitationId}").hasAnyRole("USER", "ADMIN")

                        //company endpoints
                        .requestMatchers(HttpMethod.POST,"/companies/").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/companies/{companyId}/contact-invitations").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/companies/{companyId}/contact-invitations").hasAnyRole("ADMIN")

                        //image endpoints
                        .requestMatchers(HttpMethod.POST,"/images/").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/images/{imageId}").hasAnyRole("USER", "ADMIN")

                        //notification endpoints
                        .requestMatchers(HttpMethod.GET,"/notifications/track/**").permitAll()

                        //user endpoints
                        .requestMatchers(HttpMethod.GET, "/users/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/me/password").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/me/profile-picture").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/me/identity-verification").hasAnyRole("USER", "ADMIN")

                        //verification endpoints
                        .requestMatchers(HttpMethod.POST, "/user-verifications/{id}").hasAnyRole("USER", "ADMIN")


                        //swagger
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs.yaml").permitAll()

                        //allow preflight requests
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()



                        .anyRequest().authenticated()
                )
                .headers(headers ->
                        headers.addHeaderWriter(new StaticHeadersWriter("X-Correlation-Id", ""))
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
