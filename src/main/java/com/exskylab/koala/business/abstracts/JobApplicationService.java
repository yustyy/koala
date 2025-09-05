package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.jobApplication.request.JobApplicationsApplicationIdPatchRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.request.JobsJobIdApplicationsPostRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobApplicationsApplicationIdPatchResponseDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsGetResponseDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsPostResponseDto;
import com.exskylab.koala.entities.JobApplication;

import java.util.List;

public interface JobApplicationService {
    JobsJobIdApplicationsPostResponseDto applyToJob(String jobId, JobsJobIdApplicationsPostRequestDto jobsJobIdApplicationsPostRequestDto);

    List<JobsJobIdApplicationsGetResponseDto> getJobApplicationsByIdJobId(String jobId);

    JobApplicationsApplicationIdPatchResponseDto respondToApplication(String applicationId, JobApplicationsApplicationIdPatchRequestDto jobApplicationsApplicationIdPatchRequestDto);
}
