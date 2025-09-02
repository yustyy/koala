package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.JobApplicationService;
import com.exskylab.koala.business.abstracts.JobService;
import com.exskylab.koala.core.constants.JobApplicationMessages;
import com.exskylab.koala.core.dtos.jobApplication.request.JobsJobIdApplicationsPostRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsGetResponseDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsPostResponseDto;
import com.exskylab.koala.core.mappers.JobApplicationMapper;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.entities.JobApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<SuccessDataResult<JobsJobIdApplicationsPostResponseDto>> applyToJob(@PathVariable String jobId, @RequestBody JobsJobIdApplicationsPostRequestDto jobsJobIdApplicationsPostResponseDto){

        JobApplication jobApplication = jobApplicationService.applyToJob(jobId, jobsJobIdApplicationsPostResponseDto);

        JobsJobIdApplicationsPostResponseDto responseDto = jobApplicationMapper.toJobsJobIdApplicationsPostResponseDto(jobApplication);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDataResult<JobsJobIdApplicationsPostResponseDto>(
                responseDto,
                JobApplicationMessages.JOB_APPLICATION_CREATED_SUCCESS,
                HttpStatus.CREATED
        ));

    }


    @GetMapping("/{jobId}/applications")
    public ResponseEntity<SuccessDataResult<List<JobsJobIdApplicationsGetResponseDto>>> getJobApplicationById(@PathVariable String jobId){

        List<JobApplication> jobApplications = jobApplicationService.getJobApplicationsByIdJobId(jobId);

        List<JobsJobIdApplicationsGetResponseDto> dtos = jobApplicationMapper.toJobsJobIdApplicationsGetResponseDtoList(jobApplications);

        return ResponseEntity.ok(new SuccessDataResult<List<JobsJobIdApplicationsGetResponseDto>>(
                dtos,
                JobApplicationMessages.JOB_APPLICATIONS_RETRIEVED_SUCCESS,
                HttpStatus.OK
        ));


    }


}
