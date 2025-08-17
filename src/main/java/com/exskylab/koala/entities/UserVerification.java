package com.exskylab.koala.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "verification_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "user_verifications")
public abstract class UserVerification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name ="id")
    private UUID id;

    @Column(name = "is_verified")
    private boolean verified;

    @ManyToOne
    @JoinColumn(name= "verified_by_id")
    private User verifiedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userVerification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationDocument> verificationDocuments;

}
