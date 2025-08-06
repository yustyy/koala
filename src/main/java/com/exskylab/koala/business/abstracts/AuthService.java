package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.auth.request.AuthCompleteRegistrationDto;
import com.exskylab.koala.core.dtos.auth.request.AuthLoginDto;
import com.exskylab.koala.core.dtos.auth.request.AuthStartRegistrationDto;
import com.exskylab.koala.core.dtos.auth.request.AuthVerifyRegistrationTokenDto;
import com.exskylab.koala.core.dtos.auth.response.AuthSetPasswordDto;
import com.exskylab.koala.core.dtos.auth.response.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface AuthService {

    void startRegistration(AuthStartRegistrationDto authStartRegistrationDto);

    AuthSetPasswordDto verifyRegistrationToken(AuthVerifyRegistrationTokenDto authVerifyRegistrationTokenDto);

    TokenResponseDto completeRegistration(AuthCompleteRegistrationDto authCompleteRegistrationDto, String ipAddress, String userAgent);

    TokenResponseDto login(AuthLoginDto authLoginDto, String ipAddress, String userAgent);
}