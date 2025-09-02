package com.exskylab.koala.core.dtos.jobAssignment.response;

import com.exskylab.koala.entities.AssignmentStatus;
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
public class ShortJobAssignmentDto {

    private UUID id;

    private AssignmentStatus status;

    private LocalDateTime assignmentDateTime;

}
