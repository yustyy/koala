package com.exskylab.koala.core.dtos.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSetPasswordDto {

    private String setPasswordToken;

    private boolean isEmployer;

}
