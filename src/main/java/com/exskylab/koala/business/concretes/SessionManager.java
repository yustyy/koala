package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.SessionService;
import com.exskylab.koala.core.dtos.session.response.CreatedSessionInfo;
import com.exskylab.koala.dataAccess.SessionDao;
import com.exskylab.koala.entities.Device;
import com.exskylab.koala.entities.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class SessionManager implements SessionService {

    private final SessionDao sessionDao;
    private final PasswordEncoder passwordEncoder;

    public SessionManager(SessionDao sessionDao, PasswordEncoder passwordEncoder) {
        this.sessionDao = sessionDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CreatedSessionInfo createSession(Device device, String ipAddress) {
        String originalToken = generateSecureRandomToken();

        String originalRefreshToken = generateSecureRandomToken();

        String hashedToken = passwordEncoder.encode(originalToken);
        String hashedRefreshToken = passwordEncoder.encode(originalRefreshToken);


        Session session = new Session();
        session.setDevice(device);
        session.setIpAddress(ipAddress);
        session.setToken(hashedToken);
        session.setRefreshToken(hashedRefreshToken);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        session.setRefreshExpiresAt(LocalDateTime.now().plusDays(30));
        session.setActive(true);

        Session savedSession = sessionDao.save(session);

        return new CreatedSessionInfo(savedSession, originalToken, originalRefreshToken);

    }

    private String generateSecureRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
