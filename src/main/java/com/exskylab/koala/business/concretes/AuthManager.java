package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.AuthService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.business.abstracts.VerificationService;
import com.exskylab.koala.core.constants.messages.AuthMessages;
import com.exskylab.koala.core.constants.messages.UserMessages;
import com.exskylab.koala.core.security.JwtService;
import com.exskylab.koala.core.utilities.exceptions.*;
import com.exskylab.koala.core.utilities.mail.EmailService;
import com.exskylab.koala.dataAccess.UserDao;
import com.exskylab.koala.entities.VerificationType;
import com.exskylab.koala.webAPI.dtos.auth.request.AuthLoginRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.request.AuthRegisterRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.request.RefreshTokenRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthLoginResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthRegisterResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.TokenResponseDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthManager implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final VerificationService verificationService;

    private final EmailService emailService;


    public AuthManager(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, VerificationService verificationService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public AuthRegisterResponseDto register(AuthRegisterRequestDto registerRequest) {
        var userToSave = registerRequest.toUser();
        if (userService.existsByEmail(userToSave.getEmail())){
            throw new EmailAlreadyExistsException(AuthMessages.EMAIL_ALREADY_EXISTS);
        }

        if (userService.existsByPhoneNumber(userToSave.getPhoneNumber())){
            throw new PhoneNumberAlreadyExistsException(AuthMessages.PHONE_NUMBER_ALREADY_EXISTS);
        }

        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));

        var savedUser = userService.save(userToSave);
        var emailVerification = verificationService.createVerification(savedUser, VerificationType.EMAIL);

        emailService.sendMail(userToSave.getEmail(), "isKoala email doğrulama",
                    "Merhaba " + savedUser.getFirstName() + ",\n\n" +
                    "Lütfen e-posta adresinizi doğrulamak için aşağıdaki bağlantıya tıklayın:\n" +
                    "http://localhost:8080/api/verification/email?token=" + emailVerification.getToken() + "\n\n" +
                    "Bu bağlantı 15 dakika sonra geçerliliğini yitirecektir.\n\n" +
                    "Teşekkürler,\n" +
                    "isKoala Ekibi"
                );


        var token = jwtService.generateToken(savedUser);

        return new AuthRegisterResponseDto(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                token
        );

    }

    @Override
    public AuthLoginResponseDto login(AuthLoginRequestDto loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()){
            throw new RequiredFieldException(AuthMessages.EMAIL_REQUIRED);
        }

        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()){
            throw new RequiredFieldException(AuthMessages.PASSWORD_REQUIRED);
        }


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        if (!authentication.isAuthenticated()){
            throw new InvalidCredentialsException(AuthMessages.INVALID_CREDENTIALS);
        }

        var user = userService.findByEmail(loginRequest.getEmail());
        if (user == null){
            throw new UserNotFoundException(UserMessages.USER_NOT_FOUND);
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException(AuthMessages.EMAIL_NOT_VERIFIED);
        }

        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var tokenExpirationDate = jwtService.extractExpiration(token);

        return new AuthLoginResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), token, refreshToken, tokenExpirationDate.toInstant());
    }

    @Override
    public TokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest) {

        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidTokenException(AuthMessages.INVALID_REFRESH_TOKEN);
        }

        String username = jwtService.extractUser(refreshToken);
        var user = userService.findByEmail(username);

        if (user == null){
            throw new UserNotFoundException(UserMessages.USER_NOT_FOUND);
        }

        String newAccessToken = jwtService.generateToken(user);
        var tokenExpirationDate = jwtService.extractExpiration(newAccessToken).toInstant();

        return new TokenResponseDto(newAccessToken, refreshToken, tokenExpirationDate);
    }

    @Override
    public void logout(String token) {
        //TODO: Implement logout functionality
    }

    @Override
    public Boolean validateToken(String token) {
        try{

            if (token == null || token.isEmpty()){
                return false;
            }

            String username = jwtService.extractUser(token);
            if (username == null || username.isEmpty()){
                return false;
            }

            var user = userService.findByEmail(username);
            if (user == null) {
                return false;
            }

            return jwtService.isTokenValid(token);

        } catch (Exception e){
            throw new InvalidTokenException(AuthMessages.INVALID_TOKEN);
        }


    }
}
