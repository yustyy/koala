package com.exskylab.koala.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth_date")
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name ="tc_identity_number", unique = true)
    private String tc_identity_number;

    @Column(name = "about")
    private String about;

    @Column(name = "qualifications")
    private String qualifications;

    @Column(name = "previous_jobs")
    private String previousJobs;

    @Column(name = "interests")
    private String interests;

    @Column(name = "reference_info")
    private String referenceInfo;

    @Column(name = "location_permission")
    private Boolean locationPermission;

    @Column(name = "manual_address")
    private String manualAddress;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserEducation> educations;


}
