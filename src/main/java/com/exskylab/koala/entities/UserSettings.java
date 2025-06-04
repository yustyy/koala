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
@Table(name = "user_settings")
public class UserSettings extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "notification_preferences", columnDefinition = "jsonb")
    private String notificationPreferences;

    @Column(name = "privacy_settings", columnDefinition = "jsonb")
    private String privacySettings;

    @Column(name = "app_settings", columnDefinition = "jsonb")
    private String appSettings;
}
