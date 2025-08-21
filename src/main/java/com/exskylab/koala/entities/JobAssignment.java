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
@Table(name ="job_assignments")
public class JobAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_application_id", referencedColumnName = "id", unique = true)
    private JobApplication application;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "assignment_date_time")
    private LocalDateTime assignmentDateTime;

    @Column(name = "expected_completion_date_time")
    private LocalDateTime expectedCompletionDateTime;

    @Column(name = "actual_completion_date_time")
    private LocalDateTime actualCompletionDateTime;

    @Column(name = "work_hours")
    private int workHours;

    @Column(name = "final_payout")
    private Float finalPayout;

    @Column(name = "notes")
    private String notes;


}
