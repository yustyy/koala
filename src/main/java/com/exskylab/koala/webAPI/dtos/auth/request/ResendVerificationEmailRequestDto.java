package com.exskylab.koala.webAPI.dtos.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResendVerificationEmailRequestDto {


    @NotBlank(message = "Email boş olamaz!")
    private String email;



}
