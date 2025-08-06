package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.AuthService;
import com.exskylab.koala.core.constants.AuthMessages;
import com.exskylab.koala.core.dtos.auth.request.AuthCompleteRegistrationDto;
import com.exskylab.koala.core.dtos.auth.request.AuthLoginDto;
import com.exskylab.koala.core.dtos.auth.request.AuthStartRegistrationDto;
import com.exskylab.koala.core.dtos.auth.request.AuthVerifyRegistrationTokenDto;
import com.exskylab.koala.core.dtos.auth.response.AuthSetPasswordDto;
import com.exskylab.koala.core.dtos.auth.response.TokenResponseDto;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.core.utilities.results.SuccessResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/startRegistration")
    public ResponseEntity<SuccessResult> startRegistration(@RequestBody @Valid AuthStartRegistrationDto authStartRegistrationDto,
                                                    HttpServletRequest request){
        authService.startRegistration(authStartRegistrationDto);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResult(AuthMessages.START_REGISTRATION_SUCCESS,
                HttpStatus.OK, request.getRequestURI()));

    }

    @PostMapping("/verifyRegistrationToken")
    public ResponseEntity<SuccessDataResult<AuthSetPasswordDto>>
                            verifyRegistrationToken(@RequestBody @Valid AuthVerifyRegistrationTokenDto authVerifyRegistrationTokenDto,
                                                    HttpServletRequest request){


        var response = authService.verifyRegistrationToken(authVerifyRegistrationTokenDto);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResult<>(response,
                AuthMessages.VERIFY_REGISTRATION_TOKEN_SUCCESS, HttpStatus.OK, request.getRequestURI()));



    }

    @PostMapping("/completeRegistration")
    public ResponseEntity<SuccessDataResult<TokenResponseDto>> completeRegistration(@RequestBody @Valid AuthCompleteRegistrationDto authCompleteRegistrationDto,
                                                                                    HttpServletRequest request){

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);

        var response = authService.completeRegistration(authCompleteRegistrationDto, ipAddress, userAgent);


        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessDataResult<>(
                        response,
                        AuthMessages.COMPLETE_REGISTRATION_SUCCESS,
                        HttpStatus.CREATED,
                        request.getRequestURI()
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
                        HttpStatus.OK,
                        request.getRequestURI()
                )
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
