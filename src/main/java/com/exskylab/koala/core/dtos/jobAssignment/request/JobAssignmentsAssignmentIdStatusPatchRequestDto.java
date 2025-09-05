package com.exskylab.koala.core.dtos.jobAssignment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobAssignmentsAssignmentIdStatusPatchRequestDto {
    private boolean accepted;
}
