package com.exskylab.koala.webAPI.dtos.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequestDto {

    @NotBlank(message = "Şifre sıfırlamak için e-posta adresi boş olamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email;



}
