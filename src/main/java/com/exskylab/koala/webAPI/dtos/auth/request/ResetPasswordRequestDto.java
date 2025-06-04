package com.exskylab.koala.webAPI.dtos.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordRequestDto {

    @NotBlank(message = "Şifre sıfırlamak için token boş olamaz.")
    private String token;

    @NotBlank(message = "Yeni şifre boş olamaz.")
    private String newPassword;

    @NotBlank(message = "Şifre onayı boş olamaz.")
    private String confirmPassword;



}
