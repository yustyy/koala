package com.exskylab.koala.core.dtos.auth.request;

import com.exskylab.koala.core.constants.AuthMessages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthCompleteRegistrationDto {

    @NotNull(message =AuthMessages.REGISTRATION_TOKEN_NOT_NULL)
    private String setPasswordToken;

    @NotNull(message = AuthMessages.PASSWORD_NOT_NULL)
    private String password;

}
