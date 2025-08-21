package com.exskylab.koala.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_applications")
public class JobApplication extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Job job;

    @Column(name = "application_date_time")
    private LocalDateTime applicationDateTime;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "notes")
    private String notes;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private JobAssignment assignment;



}
