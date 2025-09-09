package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.JobAssignmentService;
import com.exskylab.koala.core.constants.JobAssignmentMessages;
import com.exskylab.koala.core.dtos.jobAssignment.request.JobAssignmentsAssignmentIdStatusPatchRequestDto;
import com.exskylab.koala.core.dtos.jobAssignment.response.JobAssignmentsAssignmentIdPaymentSessionInitiatePostResponseDto;
import com.exskylab.koala.core.dtos.jobAssignment.response.JobAssignmentsAssignmentIdStatusPatchResponseDto;
import com.exskylab.koala.core.utilities.payment.iyzico.dtos.PaymentSessionResponseDto;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/job-assignments")
public class JobAssignmentsController {

    private final JobAssignmentService jobAssignmentService;


    public JobAssignmentsController(JobAssignmentService jobAssignmentService) {
        this.jobAssignmentService = jobAssignmentService;
    }




    @PatchMapping("/{assignmentId}/status")
    public ResponseEntity<SuccessDataResult<JobAssignmentsAssignmentIdStatusPatchResponseDto>> answerToAssignment(@PathVariable UUID assignmentId,
            @RequestBody @Valid JobAssignmentsAssignmentIdStatusPatchRequestDto jobAssignmentsAssignmentIdStatusPatchRequestDto){

        JobAssignmentsAssignmentIdStatusPatchResponseDto response = jobAssignmentService.answerToAssignment(assignmentId, jobAssignmentsAssignmentIdStatusPatchRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new SuccessDataResult<>(
                                response,
                                JobAssignmentMessages.ANSWER_SUCCESS,
                                HttpStatus.OK
                        )
                );
    }


    @PostMapping("/{assignmentId}/payment-session/initiate")
    public ResponseEntity<SuccessDataResult<JobAssignmentsAssignmentIdPaymentSessionInitiatePostResponseDto>> initiatePaymentSession(@PathVariable UUID assignmentId, HttpServletRequest request){
        JobAssignmentsAssignmentIdPaymentSessionInitiatePostResponseDto dto = jobAssignmentService.initiatePaymentSession(assignmentId, getClientIpAddress(request));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessDataResult<>(
                        dto,
                        JobAssignmentMessages.PAYMENT_SESSION_INITIATED,
                        HttpStatus.OK
                ));
    }



    private String getClientIpAddress(HttpServletRequest request) {
        String remoteAddr = "";
        if(request != null){
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)){
                remoteAddr = request.getRemoteAddr();
            }
        }

        return  remoteAddr;

    }


}
