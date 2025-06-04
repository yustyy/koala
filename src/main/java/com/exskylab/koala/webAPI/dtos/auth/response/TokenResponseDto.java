package com.exskylab.koala.webAPI.dtos.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String token;
    private String refreshToken;
    private Instant expiresAt;
}