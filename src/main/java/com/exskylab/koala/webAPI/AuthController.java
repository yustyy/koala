package com.exskylab.koala.webAPI;


import com.exskylab.koala.business.abstracts.AuthService;
import com.exskylab.koala.core.constants.messages.AuthMessages;
import com.exskylab.koala.core.constants.messages.UserMessages;
import com.exskylab.koala.core.utilities.results.DataResult;
import com.exskylab.koala.core.utilities.results.Result;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.core.utilities.results.SuccessResult;
import com.exskylab.koala.webAPI.dtos.auth.request.*;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthLoginResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthRegisterResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<DataResult<AuthRegisterResponseDto>> register(@Valid @RequestBody AuthRegisterRequestDto authRegisterRequestDto, HttpServletRequest request){

        var resultDto = authService.register(authRegisterRequestDto);
        return ResponseEntity.status(201).body(
                new SuccessDataResult<>(resultDto, UserMessages.REGISTER_SUCCESS, HttpStatus.CREATED, request.getRequestURI()));

    }

    @PostMapping("/login")
    public ResponseEntity<DataResult<AuthLoginResponseDto>> login(@Valid @RequestBody AuthLoginRequestDto authLoginRequestDto, HttpServletRequest request) {
        var resultDto = authService.login(authLoginRequestDto);
        return ResponseEntity.ok(
                new SuccessDataResult<>(resultDto, UserMessages.LOGIN_SUCCESS, HttpStatus.OK, request.getRequestURI()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<DataResult<TokenResponseDto>>  refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshToken, HttpServletRequest request) {
        var resultDto = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(
                new SuccessDataResult<>(resultDto, UserMessages.REFRESH_TOKEN_SUCCESS, HttpStatus.OK, request.getRequestURI()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Result> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto, HttpServletRequest request) {
        authService.forgotPassword(forgotPasswordRequestDto.getEmail());
        return ResponseEntity.ok(
                new SuccessResult(UserMessages.FORGOT_PASSWORD_SUCCESS, HttpStatus.OK, request.getRequestURI()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Result> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto, HttpServletRequest request){
        authService.resetPassword(resetPasswordRequestDto);
        return ResponseEntity.ok().body(new SuccessResult(UserMessages.RESET_PASSWORD_SUCCESS, HttpStatus.OK, request.getRequestURI()));
    }

    @PostMapping("/verify-reset-token")
    public ResponseEntity<Result> verifyResetToken(@RequestParam @NotBlank(message = AuthMessages.TOKEN_REQUIRED) String token, HttpServletRequest request) {
        authService.verifyResetToken(token);
        return ResponseEntity.ok().body(new SuccessResult(UserMessages.VERIFY_RESET_TOKEN_SUCCESS, HttpStatus.OK, request.getRequestURI()));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Result> verifyEmail(@RequestParam @NotBlank(message = AuthMessages.TOKEN_REQUIRED) String token, HttpServletRequest request) {
        authService.verifyEmailWithToken(token);
        return ResponseEntity.ok().body(new SuccessResult(UserMessages.VERIFY_EMAIL_SUCCESS, HttpStatus.OK, request.getRequestURI()));
    }





}
