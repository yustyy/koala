package com.exskylab.koala.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_ibans")
public class UserIban extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "iban", nullable = false, unique = true)
    private String iban;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "active", nullable = false)
    private boolean active;



}
