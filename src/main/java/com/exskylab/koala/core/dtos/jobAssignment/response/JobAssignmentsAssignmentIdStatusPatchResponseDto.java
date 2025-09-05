package com.exskylab.koala.core.dtos.jobAssignment.response;

import com.exskylab.koala.core.dtos.company.response.OnlyCompanyDto;
import com.exskylab.koala.core.dtos.job.response.ShortJobDto;
import com.exskylab.koala.core.dtos.user.response.SafeUserDto;
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
public class JobAssignmentsAssignmentIdStatusPatchResponseDto {

    private UUID id;

    private LocalDateTime assignmentDateTime;

    private AssignmentStatus status;

    private ShortJobDto job;

    private SafeUserDto worker;

    private OnlyCompanyDto company;

}
