package com.exskylab.koala.core.security;

import com.exskylab.koala.business.abstracts.SessionService;
import com.exskylab.koala.business.abstracts.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class SessionAuthFilter extends OncePerRequestFilter {

    private final SessionService sessionService;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(SessionAuthFilter.class);


    public SessionAuthFilter(SessionService sessionService, JwtService jwtService) {
        this.sessionService = sessionService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Processing authentication for request: {}", request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String sessionToken = authHeader.substring(7);

        if (SecurityContextHolder.getContext().getAuthentication() != null){
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
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));



            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.info("Authenticated user ID: {} with session ID: {}", userId, sessionId);

        }catch (Exception e){
            logger.info("Failed to authenticate session token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

