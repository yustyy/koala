package com.exskylab.koala.core.security;

import com.exskylab.koala.business.abstracts.DeviceService;
import com.exskylab.koala.business.abstracts.SessionService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.constants.AuthMessages;
import com.exskylab.koala.core.exceptions.DeviceDoesntMatchException;
import com.exskylab.koala.core.exceptions.NotValidAuthorizationHeaderException;
import com.exskylab.koala.core.exceptions.SessionDoesntMatch;
import com.exskylab.koala.core.exceptions.SessionNotFoundException;
import com.exskylab.koala.entities.Device;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SessionAuthFilter extends OncePerRequestFilter {

    private final SessionService sessionService;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(SessionAuthFilter.class);
    private final DeviceService deviceService;

    private final HandlerExceptionResolver exceptionResolver;


    public SessionAuthFilter(SessionService sessionService, JwtService jwtService, DeviceService deviceService,
                             @Qualifier("handlerExceptionResolver")HandlerExceptionResolver exceptionResolver) {
        this.sessionService = sessionService;
        this.jwtService = jwtService;
        this.deviceService = deviceService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Processing authentication for request: {}", request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("No valid Authorization header found in request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String sessionToken = authHeader.substring(7);

        if (SecurityContextHolder.getContext().getAuthentication() != null){
            logger.info("User is already authenticated. Skipping session authentication for request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try{
            final UUID sessionId = jwtService.extractSessionId(sessionToken);
            final UUID userId = jwtService.extractUserId(sessionToken);

            var session = sessionService.findActiveSessionById(sessionId);
            var user = session.getDevice().getUser();

            if (!session.getDevice().getUser().getId().equals(userId)){
                logger.info("Session ID: {} is not associated with user ID: {}.", sessionId, userId);
                exceptionResolver.resolveException(request,response,null,
                        new SessionDoesntMatch(AuthMessages.SESSION_NOT_MATCHING_USER));
                return;
            }

            Device currentDevice;
            try{
                currentDevice = deviceService.findDeviceByUserAndUserAgent(
                        user,
                        request.getHeader("User-Agent")
                );
            }catch (Exception e){
                currentDevice = null;
                logger.error("Users token doesnt match with current device. Deactivating session ID: {}", session.getId());
            }

            if (currentDevice == null || !session.getDevice().getId().equals(currentDevice.getId())){
                session.setActive(false);
                session.setLastAccessedAt(LocalDateTime.now());
                sessionService.save(session);
                logger.error("SECURITY ALERT: Session ID: {} is not associated with the current device. Session has been deactivated.", sessionId);
                exceptionResolver.resolveException(request,response,null,
                        new DeviceDoesntMatchException(AuthMessages.SESSION_NOT_MATCHING_CURRENT_DEVICE));
                return;
            }


            session.setLastAccessedAt(LocalDateTime.now());
            sessionService.save(session);


            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));



            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.info("Authenticated user ID: {} with session ID: {}", userId, sessionId);

        }catch (JwtException | SessionNotFoundException | DeviceDoesntMatchException e){
            logger.warn("Authentication failed for request {}: {}", request.getRequestURI(), e.getMessage());
            exceptionResolver.resolveException(request,response,null, e);
            return;
        }catch (Exception e){
            logger.info("Failed to authenticate session token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

