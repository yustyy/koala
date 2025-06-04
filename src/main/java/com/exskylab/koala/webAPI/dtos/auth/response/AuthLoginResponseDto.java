package com.exskylab.koala.webAPI.dtos.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private String refreshToken;
    private Instant expiresAt;
}