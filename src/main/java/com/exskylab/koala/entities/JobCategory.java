package com.exskylab.koala.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_categories")
public class JobCategory extends BaseEntity{


    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @jakarta.persistence.Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;



}
