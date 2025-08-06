package com.exskylab.koala.core.dtos.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
}