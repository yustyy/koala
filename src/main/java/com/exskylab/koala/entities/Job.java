package com.exskylab.koala.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobs")
public class Job extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "job_category_id")
    private JobCategory category;

    @Enumerated(EnumType.STRING)
    private JobEmployerType employerType;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "sector")
    private String sector;

    @Column(name = "position")
    private String position;

    @Column(name = "location")
    private String duties;

    @Column(name = "insurance_required")
    private boolean insuranceRequired;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private SalaryType salaryType;

    @Column(name = "salary")
    private double salary;

    @Column(name = "min_age")
    private int minAge;

    @Column(name = "max_age")
    private int maxAge;

    @Column(name = "experience_required")
    private boolean experienceRequired;

    @Column(name = "dress_code")
    private String dressCode;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobApplication> applications;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobAssignment> assignments;

}
