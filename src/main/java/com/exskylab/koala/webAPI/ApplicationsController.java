package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.JobApplicationService;
import com.exskylab.koala.core.constants.JobApplicationMessages;
import com.exskylab.koala.core.dtos.jobApplication.request.JobApplicationsApplicationIdPatchRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobApplicationsApplicationIdPatchResponseDto;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-applications")
public class ApplicationsController {

    private final JobApplicationService jobApplicationService;


    public ApplicationsController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }


    @PatchMapping("/{applicationId}")
    public ResponseEntity<SuccessDataResult<JobApplicationsApplicationIdPatchResponseDto>> respondToApplication(@PathVariable String applicationId,
                                                                                                                @RequestBody @Valid JobApplicationsApplicationIdPatchRequestDto jobApplicationsApplicationIdPatchRequestDto){

        JobApplicationsApplicationIdPatchResponseDto dto = jobApplicationService.respondToApplication(applicationId, jobApplicationsApplicationIdPatchRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResult<JobApplicationsApplicationIdPatchResponseDto>(
                dto,
                JobApplicationMessages.JOB_APPLICATION_RESPONDED_SUCCESS,
                HttpStatus.OK
                )
        );

    }
}
