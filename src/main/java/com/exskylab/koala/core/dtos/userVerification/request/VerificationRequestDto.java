package com.exskylab.koala.core.dtos.userVerification.request;

import com.exskylab.koala.core.constants.UserVerificationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequestDto {

    @NotBlank(message = UserVerificationMessages.TOKEN_NOT_BLANK)
    private String token;


}
