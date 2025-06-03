package com.exskylab.koala.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "jobs")
public class Job extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "sector", nullable = false)
    private String sector;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name ="duties")
    private String duties;

    @Column(name = "location_lat")
    private Float locationLatitude;

    @Column(name = "location_lng")
    private Float locationLongitude;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private SalaryType salaryType;

    @Column(name = "salary", nullable = false)
    private String salary;

    @Column(name = "age_min")
    private int age_min;

    @Column(name = "age_max")
    private int age_max;

    @Column(name = "experience_required")
    private boolean experienceRequired;

    @Column(name = "dress_code")
    private String dressCode;

    @Column(name = "featured")
    private boolean featured;

    @Enumerated(EnumType.STRING)
    private JobStatus status;



}
