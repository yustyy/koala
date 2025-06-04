package com.exskylab.koala.webAPI.dtos.auth.request;

import com.exskylab.koala.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequestDto {

    @NotBlank(message = "Ad boş olamaz")
    @Size(min = 2, max = 20, message = "Ad 2-20 karakter arası olmalıdır")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    @Size(min = 2, max = 50, message = "Soyad 2-20 karakter arası olmalıdır")
    private String lastName;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "Şifre en az bir harf ve bir rakam içermelidir")
    private String password;

    @Pattern(regexp = "^[0-9]{10}$", message = "Telefon numarası 10 rakamdan oluşmalıdır")
    private String phoneNumber;

    public User toUser() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
    }
}