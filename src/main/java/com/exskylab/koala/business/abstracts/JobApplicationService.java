package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.jobApplication.request.JobApplicationsApplicationIdPatchRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.request.JobsJobIdApplicationsPostRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobApplicationsApplicationIdPatchResponseDto;
import com.exskylab.koala.entities.JobApplication;
import jakarta.validation.Valid;

import java.util.List;

public interface JobApplicationService {
    JobApplication applyToJob(String jobId, JobsJobIdApplicationsPostRequestDto jobsJobIdApplicationsPostRequestDto);

    List<JobApplication> getJobApplicationsByIdJobId(String jobId);

    JobApplicationsApplicationIdPatchResponseDto respondToApplication(String applicationId, JobApplicationsApplicationIdPatchRequestDto jobApplicationsApplicationIdPatchRequestDto);
}
