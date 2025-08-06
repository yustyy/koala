package com.exskylab.koala.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("IDENTITY")
public class IdentityVerification extends UserVerification {

    @Column(name = "tc_identity_number")
    private String tcIdentityNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "verification_document_id")
    private VerificationDocument verificationDocument;
}