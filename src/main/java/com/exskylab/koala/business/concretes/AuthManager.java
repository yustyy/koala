package com.exskylab.koala.business.concretes;


import com.exskylab.koala.business.abstracts.*;
import com.exskylab.koala.core.configs.AppProperties;
import com.exskylab.koala.core.constants.AuthMessages;
import com.exskylab.koala.core.dtos.auth.request.AuthCompleteRegistrationDto;
import com.exskylab.koala.core.dtos.auth.request.AuthLoginDto;
import com.exskylab.koala.core.dtos.auth.request.AuthStartRegistrationDto;
import com.exskylab.koala.core.dtos.auth.request.AuthVerifyRegistrationTokenDto;
import com.exskylab.koala.core.dtos.auth.response.AuthSetPasswordDto;
import com.exskylab.koala.core.dtos.auth.response.TokenResponseDto;
import com.exskylab.koala.core.dtos.notification.request.SendEmailDto;
import com.exskylab.koala.core.dtos.session.response.CreatedSessionInfo;
import com.exskylab.koala.core.dtos.user.CreateUserDto;
import com.exskylab.koala.core.exceptions.EmailOrPasswordMismatchException;
import com.exskylab.koala.core.exceptions.TokenExpiredException;
import com.exskylab.koala.core.exceptions.UserAlreadyExistsException;
import com.exskylab.koala.core.security.JwtService;
import com.exskylab.koala.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Service
public class AuthManager implements AuthService {

    private final AppProperties appProperties;
    private final UserService userService;
    private final PendingRegistrationService pendingRegistrationService;
    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final DeviceService deviceService;
    private final SessionService sessionService;
    private final LoginAttemptService loginAttemptService;

    private Logger logger = LoggerFactory.getLogger(AuthManager.class);

    public AuthManager(UserService userService,
                       PendingRegistrationService pendingRegistrationService,
                       NotificationService notificationService,
                       AppProperties appProperties,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       DeviceService deviceService,
                       SessionService sessionService,
                       LoginAttemptService loginAttemptService) {
        this.userService = userService;
        this.pendingRegistrationService = pendingRegistrationService;
        this.notificationService = notificationService;
        this.appProperties = appProperties;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.deviceService = deviceService;
        this.sessionService = sessionService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    @Transactional
    public void startRegistration(AuthStartRegistrationDto authStartRegistrationDto) {
        logger.info("Starting registration for email: {}", authStartRegistrationDto.getEmail());

        //checking if user alrdy exists
        if (userService.existsByEmail(authStartRegistrationDto.getEmail())){
            logger.warn("User with email {} already exists", authStartRegistrationDto.getEmail());
            throw new UserAlreadyExistsException(AuthMessages.USER_ALREADY_EXISTS_WITH_THIS_EMAIL);
        }

        //creating pending registration
        PendingRegistration pendingRegistration = null;
        try{
            pendingRegistration = pendingRegistrationService.getByEmail(authStartRegistrationDto.getEmail());
        }catch (Exception e){
            logger.info("No pending registration found for email: {}, creating a new one.", authStartRegistrationDto.getEmail());
        }

        if (pendingRegistration != null && pendingRegistration.getExpiresAt().isBefore(LocalDateTime.now())){
            logger.info("user already has a pending registration, but it has expired. Deleting the old one. email: {}", authStartRegistrationDto.getEmail());
            pendingRegistrationService.deleteById(pendingRegistration.getId());
        }
        if (pendingRegistration != null && pendingRegistration.getExpiresAt().isAfter(LocalDateTime.now())){
            logger.info("User already has a pending registration, no need to create a new one. email: {}", authStartRegistrationDto.getEmail());
            throw new UserAlreadyExistsException(AuthMessages.PENDING_REGISTRATION_ALREADY_EXISTS);
        }

        pendingRegistration = pendingRegistrationService.createPendingRegistration(authStartRegistrationDto);
        logger.info("Pending registration created for email: {}", authStartRegistrationDto.getEmail());

        String verificationLink = appProperties.frontendUrl()+ "/complete-registration?token=" + pendingRegistration.getToken();

        Map<String, Object> emailParams = Map.of(
                "firstName", authStartRegistrationDto.getFirstName(),
                "lastName", authStartRegistrationDto.getLastName(),
                "verificationLink", verificationLink
        );

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setCategory(NotificationCategory.ACCOUNT_SECURITY);
        sendEmailDto.setDestinationEmail(authStartRegistrationDto.getEmail());
        sendEmailDto.setTemplateName("registration-verification-template");
        sendEmailDto.setTemplateParameters(emailParams);
        sendEmailDto.setSubject("Koala kayıt işlemi için e-posta doğrulaması");

        notificationService.sendEmail(sendEmailDto, DispatchPriority.CRITICAL, true);
        logger.info("Verification email sent to: {}", authStartRegistrationDto.getEmail());

        logger.info("Registration process started successfully for email: {}", authStartRegistrationDto.getEmail());
    }

    @Override
    public AuthSetPasswordDto verifyRegistrationToken(AuthVerifyRegistrationTokenDto authVerifyRegistrationTokenDto) {
        logger.info("Verifying registration token for id: {}", authVerifyRegistrationTokenDto.getRegistrationToken());

        PendingRegistration pendingRegistration = pendingRegistrationService.getByToken(authVerifyRegistrationTokenDto.getRegistrationToken());

        if (pendingRegistration.getExpiresAt().isBefore(LocalDateTime.now())){
            pendingRegistrationService.deleteById(pendingRegistration.getId());
            logger.warn("Registration token for email: {} has expired", pendingRegistration.getEmail());
            throw new TokenExpiredException(AuthMessages.REGISTRATION_TOKEN_EXPIRED);
        }

        String passwordSetToken = jwtService.generatePasswordSetToken(pendingRegistration.getId());
        logger.info("Token verified successfully for email: {}", pendingRegistration.getEmail());

        return new AuthSetPasswordDto(passwordSetToken);
    }

    @Override
    @Transactional
    public TokenResponseDto completeRegistration(AuthCompleteRegistrationDto authCompleteRegistrationDto,
                                                 String ipAddress, String userAgent) {
        UUID pendingRegistrationId = jwtService.extractPendingRegistrationId(authCompleteRegistrationDto.getSetPasswordToken());

        PendingRegistration pendingRegistration = pendingRegistrationService.getById(pendingRegistrationId);

        logger.info("Completing registration for email: {}", pendingRegistration.getEmail());


        User newUser = new User();
        newUser.setFirstName(pendingRegistration.getFirstName());
        newUser.setLastName(pendingRegistration.getLastName());
        newUser.setEmail(pendingRegistration.getEmail());
        newUser.setPassword(passwordEncoder.encode(authCompleteRegistrationDto.getPassword()));
        newUser.setAuthorities(Set.of(Role.ROLE_USER));

        newUser.setEmailVerified(true);
        newUser.setPhoneVerified(false);
        newUser.setIdentityVerified(false);
        newUser.setCreatedBy(userService.getSystemUser());

        User savedUser = userService.save(newUser);

        logger.info("User created successfully with ID: {}", savedUser.getId());

        pendingRegistrationService.deleteById(pendingRegistrationId);

        Device device = deviceService.findOrCreateDevice(
                savedUser,
                ipAddress,
                userAgent
        );

        logger.info("Device created or found for user ID: {}", savedUser.getId());

        CreatedSessionInfo createdSessionInfo = sessionService.createSession(device, ipAddress);
        logger.info("Session created for user ID: {}, session ID: {}", savedUser.getId(), createdSessionInfo.session().getId());

        LoginAttempt attempt = new LoginAttempt();
        attempt.setUser(savedUser);
        attempt.setEmailAttempted(savedUser.getEmail());
        attempt.setSuccess(true);
        attempt.setIpAddress(ipAddress);
        attempt.setDeviceName(device.getName());
        loginAttemptService.save(attempt);

        sendWelcomeEmail(savedUser);

        return new TokenResponseDto(
                createdSessionInfo.originalToken(),
                createdSessionInfo.originalRefreshToken(),
                createdSessionInfo.session().getRefreshExpiresAt()
        );

    }

    @Override
    public TokenResponseDto login(AuthLoginDto authLoginDto, String ipAddress, String userAgent) {
        logger.info("Logging in user with email: {}", authLoginDto.getEmail());

        var user = userService.getByEmail(authLoginDto.getEmail());

        //no need to check if user exists, because it is already checked in UserService.getByEmail() if its not then it will throw exception
        if (!passwordEncoder.matches(authLoginDto.getPassword(), user.getPassword())){
            LoginAttempt loginAttempt = new LoginAttempt();
            loginAttempt.setEmailAttempted(authLoginDto.getEmail());
            loginAttempt.setSuccess(false);
            loginAttempt.setFailureReason("Invalid email or password");
            loginAttempt.setIpAddress(ipAddress);
            loginAttempt.setDeviceName(userAgent);
            loginAttempt.setUser(user);
            loginAttemptService.save(loginAttempt);

            throw new EmailOrPasswordMismatchException(AuthMessages.EMAIL_OR_PASSWORD_MISMATCH);
        }

        Device device = deviceService.findOrCreateDevice(
                user,
                ipAddress,
                userAgent
        );

        logger.info("Device found or created for user ID: {}", user.getId());


        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUser(user);
        loginAttempt.setEmailAttempted(user.getEmail());
        loginAttempt.setSuccess(true);
        loginAttempt.setIpAddress(ipAddress);
        loginAttempt.setDeviceName(device.getName());
        loginAttemptService.save(loginAttempt);

        CreatedSessionInfo createdSessionInfo = sessionService.createSession(device, ipAddress);


        logger.info("Session created for user ID: {}, session ID: {}", user.getId(), createdSessionInfo.session().getId());
        return new TokenResponseDto(
                createdSessionInfo.originalToken(),
                createdSessionInfo.originalRefreshToken(),
                createdSessionInfo.session().getRefreshExpiresAt()
        );

    }

    private void sendWelcomeEmail(User savedUser) {
        logger.debug("Sending welcome email to user with ID: {}", savedUser.getId());

            SendEmailDto sendEmailDto = new SendEmailDto();
            sendEmailDto.setRecipientId(savedUser.getId());
            sendEmailDto.setCategory(NotificationCategory.ACCOUNT_SECURITY);
            sendEmailDto.setDestinationEmail(savedUser.getEmail());
            sendEmailDto.setSubject("Koala'ya Hoş Geldin "+ savedUser.getFirstName()+ "!");
            sendEmailDto.setTemplateName("welcome-email-template");
            sendEmailDto.setTemplateParameters(Map.of(
                    "firstName", savedUser.getFirstName(),
                    "lastName", savedUser.getLastName(),
                    "email", savedUser.getEmail()));

            notificationService.sendEmail(sendEmailDto, DispatchPriority.NORMAL, true);
    }

}
