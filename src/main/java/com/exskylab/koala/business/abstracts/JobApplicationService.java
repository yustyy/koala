package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.jobApplication.request.JobsJobIdApplicationsRequestDto;
import com.exskylab.koala.entities.JobApplication;

public interface JobApplicationService {
    JobApplication applyToJob(String jobId, JobsJobIdApplicationsRequestDto jobsJobIdApplicationsRequestDto);

}
