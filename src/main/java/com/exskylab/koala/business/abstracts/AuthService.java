package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.User;
import com.exskylab.koala.webAPI.dtos.auth.request.AuthLoginRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.request.AuthRegisterRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.request.RefreshTokenRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.request.ResetPasswordRequestDto;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthLoginResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.AuthRegisterResponseDto;
import com.exskylab.koala.webAPI.dtos.auth.response.TokenResponseDto;
import jakarta.validation.Valid;

public interface AuthService {


    AuthRegisterResponseDto register(AuthRegisterRequestDto registerRequest);

    AuthLoginResponseDto login(AuthLoginRequestDto loginRequest);

    TokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest);

    void logout(String token);

    boolean validateToken(String token);

    boolean forgotPassword(String email);

    boolean resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);

    boolean verifyResetToken(String token);

    boolean verifyEmailWithToken(String token);

    boolean resendVerificationEmail(String email);

}