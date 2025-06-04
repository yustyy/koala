package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.AuthService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.constants.messages.AuthMessages;
import com.exskylab.koala.core.security.JwtService;
import com.exskylab.koala.dataAccess.UserDao;
import com.exskylab.koala.webAPI.dtos.auth.request.AuthLoginRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.request.AuthRegisterRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.request.RefreshTokenRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthLoginResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthRegisterResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.TokenResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthManager implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;



    public AuthManager(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public AuthRegisterResponseDto register(AuthRegisterRequestDto registerRequest) {
        var userToSave = registerRequest.toUser();
        if (userService.existsByEmail(userToSave.getEmail())){
            throw new IllegalArgumentException(AuthMessages.EMAIL_ALREADY_EXISTS);
        }

        if (userService.existsByPhoneNumber(userToSave.getPhoneNumber())){
            throw new IllegalArgumentException(AuthMessages.PHONE_NUMBER_ALREADY_EXISTS);
        }

        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));

        var savedUser = userService.save(userToSave);
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
        return null;
    }

    @Override
    public TokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest) {
        return null;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public Boolean validateToken(String token) {
        return null;
    }
}
