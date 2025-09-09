package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.jobAssignment.request.JobAssignmentsAssignmentIdStatusPatchRequestDto;
import com.exskylab.koala.core.dtos.jobAssignment.response.JobAssignmentsAssignmentIdPaymentSessionInitiatePostResponseDto;
import com.exskylab.koala.core.dtos.jobAssignment.response.JobAssignmentsAssignmentIdStatusPatchResponseDto;
import com.exskylab.koala.entities.JobAssignment;
import jakarta.validation.Valid;

import java.util.UUID;

public interface JobAssignmentService {
    JobAssignment save(JobAssignment jobAssignment);

    JobAssignmentsAssignmentIdStatusPatchResponseDto answerToAssignment(UUID assignmentId, JobAssignmentsAssignmentIdStatusPatchRequestDto jobAssignmentsAssignmentIdStatusPatchRequestDto);

    JobAssignmentsAssignmentIdPaymentSessionInitiatePostResponseDto initiatePaymentSession(UUID assignmentId, String clientIpAddress);

}
