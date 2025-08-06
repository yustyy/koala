package com.exskylab.koala.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NotNull
@Table(name = "sessions")
public class Session extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(name = "refresh_token_hash", length = 512)
    private String refreshTokenHash;

    @Column(name = "refresh_expires_at")
    private LocalDateTime refreshExpiresAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "location")
    private String location;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "is_active")
    private boolean isActive = true;


}
