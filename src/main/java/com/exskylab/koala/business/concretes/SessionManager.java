package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.SessionService;
import com.exskylab.koala.core.constants.SessionMessages;
import com.exskylab.koala.core.dtos.session.response.CreatedSessionInfo;
import com.exskylab.koala.core.exceptions.SessionNotFoundException;
import com.exskylab.koala.core.exceptions.UserNotFoundException;
import com.exskylab.koala.core.security.JwtService;
import com.exskylab.koala.dataAccess.SessionDao;
import com.exskylab.koala.entities.Device;
import com.exskylab.koala.entities.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class SessionManager implements SessionService {

    private final SessionDao sessionDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private Logger logger = LoggerFactory.getLogger(SessionManager.class);

    public SessionManager(SessionDao sessionDao, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.sessionDao = sessionDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public CreatedSessionInfo createSession(Device device, String ipAddress) {
        logger.info("Creating session for device: {}", device.getId());

        var user = device.getUser();
        if (user == null){
            logger.error("Device with id: {} does not have an associated user.", device.getId());
            throw new UserNotFoundException(SessionMessages.USER_NOT_FOUND_FOR_DEVICE);
        }

        String originalRefreshToken = generateSecureRandomToken();
        String hashedRefreshToken = passwordEncoder.encode(originalRefreshToken);


        Session session = new Session();
        session.setDevice(device);
        session.setIpAddress(ipAddress);
        session.setRefreshTokenHash(hashedRefreshToken);
        session.setRefreshExpiresAt(LocalDateTime.now().plusDays(30));
        session.setActive(true);
        session.setLastAccessedAt(LocalDateTime.now());

        Session savedSession = sessionDao.save(session);

        String accessToken = jwtService.generateAccessToken(user, savedSession.getId());

        return new CreatedSessionInfo(savedSession, accessToken, originalRefreshToken);
    }


    @Override
    public Session findActiveSessionById(UUID sessionId) {
        return sessionDao.findByIdAndIsActive(sessionId, true)
                .orElseThrow(() -> new SessionNotFoundException(SessionMessages.SESSION_NOT_FOUND));
    }

    @Override
    public Session save(Session session) {
        logger.info("Saving session with id: {}", session.getId());
        return sessionDao.save(session);
    }

    private String generateSecureRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
