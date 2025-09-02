package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.JobApplicationService;
import com.exskylab.koala.business.abstracts.JobService;
import com.exskylab.koala.core.constants.JobApplicationMessages;
import com.exskylab.koala.core.dtos.jobApplication.request.JobsJobIdApplicationsRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsResponseDto;
import com.exskylab.koala.core.mappers.JobApplicationMapper;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.entities.JobApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobsController {

    private final JobService jobService;
    private final JobApplicationService jobApplicationService;
    private final JobApplicationMapper jobApplicationMapper;


    public JobsController(JobService jobService, JobApplicationService jobApplicationService, JobApplicationMapper jobApplicationMapper) {
        this.jobService = jobService;
        this.jobApplicationService = jobApplicationService;
        this.jobApplicationMapper = jobApplicationMapper;
    }



    @PostMapping("/{jobId}/applications")
    public ResponseEntity<SuccessDataResult<JobsJobIdApplicationsResponseDto>> applyToJob(@PathVariable String jobId, @RequestBody JobsJobIdApplicationsRequestDto jobsJobIdApplicationsRequestDto){

        JobApplication jobApplication = jobApplicationService.applyToJob(jobId, jobsJobIdApplicationsRequestDto);

        JobsJobIdApplicationsResponseDto responseDto = jobApplicationMapper.toJobsJobIdApplicationsResponseDto(jobApplication);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDataResult<JobsJobIdApplicationsResponseDto>(
                responseDto,
                JobApplicationMessages.JOB_APPLICATION_CREATED_SUCCESS,
                HttpStatus.CREATED
        ));

    }
}
