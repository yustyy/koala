package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.AuthService;
import com.exskylab.koala.core.constants.AuthMessages;
import com.exskylab.koala.core.dtos.auth.request.*;
import com.exskylab.koala.core.dtos.auth.response.AuthSetPasswordDto;
import com.exskylab.koala.core.dtos.auth.response.TokenResponseDto;
import com.exskylab.koala.core.security.JwtService;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.core.utilities.results.SuccessResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;


    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/start-registration")
    public ResponseEntity<SuccessResult> startRegistration(@RequestBody @Valid AuthStartRegistrationDto authStartRegistrationDto){
        authService.startRegistration(authStartRegistrationDto);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResult(AuthMessages.START_REGISTRATION_SUCCESS,
                HttpStatus.OK));

    }

    @PostMapping("/verify-registration-token")
    public ResponseEntity<SuccessDataResult<AuthSetPasswordDto>>
                            verifyRegistrationToken(@RequestBody @Valid AuthVerifyRegistrationTokenDto authVerifyRegistrationTokenDto){


        var response = authService.verifyRegistrationToken(authVerifyRegistrationTokenDto);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResult<>(response,
                AuthMessages.VERIFY_REGISTRATION_TOKEN_SUCCESS, HttpStatus.OK));



    }

    @PostMapping("/complete-registration")
    public ResponseEntity<SuccessDataResult<TokenResponseDto>> completeRegistration(@RequestBody @Valid AuthCompleteRegistrationDto authCompleteRegistrationDto,
                                                                                    HttpServletRequest request){

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);

        var response = authService.completeRegistration(authCompleteRegistrationDto, ipAddress, userAgent);


        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessDataResult<>(
                        response,
                        AuthMessages.COMPLETE_REGISTRATION_SUCCESS,
                        HttpStatus.CREATED
            )
        );


    }

    @PostMapping("/login")
    public ResponseEntity<SuccessDataResult<TokenResponseDto>> login(@RequestBody @Valid AuthLoginDto authLoginDto, HttpServletRequest request){

        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");

        var response = authService.login(authLoginDto, ipAddress, userAgent);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessDataResult<>(
                        response,
                        AuthMessages.LOGIN_SUCCESS,
                        HttpStatus.OK
                )
        );

    }


    @PostMapping("/refreshToken")
    public ResponseEntity<SuccessDataResult<TokenResponseDto>> refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto, HttpServletRequest request){
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        String authHeader = request.getHeader("Authorization");
        String expiredToken = authHeader.substring(7);
        UUID sessionId = jwtService.extractSessionId(expiredToken);

        var response = authService.refreshToken(refreshTokenDto, sessionId, ipAddress, userAgent);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessDataResult<>(
                        response,
                        AuthMessages.REFRESH_TOKEN_SUCCESS,
                        HttpStatus.OK
                )
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<SuccessResult> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        UUID sessionId = jwtService.extractSessionId(token);

        authService.logout(sessionId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessResult(AuthMessages.LOGOUT_SUCCESS, HttpStatus.OK)
        );
    }



    private String getClientIpAddress(HttpServletRequest request) {
        String remoteAddr = "";
        if(request != null){
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)){
                remoteAddr = request.getRemoteAddr();
            }
        }

        return  remoteAddr;

    }


}
