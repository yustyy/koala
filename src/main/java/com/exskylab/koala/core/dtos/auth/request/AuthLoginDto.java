package com.exskylab.koala.core.dtos.auth.request;

import com.exskylab.koala.core.constants.AuthMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginDto {

    @Email(message = AuthMessages.INVALID_EMAIL)
    @NotNull(message = AuthMessages.EMAIL_NOT_NULL)
    private String email;

    @NotNull(message = AuthMessages.PASSWORD_NOT_NULL)
    private String password;

}
