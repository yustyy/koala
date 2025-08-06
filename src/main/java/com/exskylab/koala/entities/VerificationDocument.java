package com.exskylab.koala.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("VERIFICATION_DOCUMENT")
public class VerificationDocument extends Media{

    @OneToOne(mappedBy = "verificationDocument")
    private UserVerification userVerification;

}
