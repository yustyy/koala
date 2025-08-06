package com.exskylab.koala.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("PHONE")
public class PhoneVerification extends UserVerification{


    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "token")
    private String token;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

}
