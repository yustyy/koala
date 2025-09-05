package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.job.request.CompaniesCompanyIdJobsPostRequestDto;
import com.exskylab.koala.core.dtos.job.request.UsersMeJobsPostRequestDto;
import com.exskylab.koala.core.dtos.job.response.CompaniesCompanyIdJobsPostResponseDto;
import com.exskylab.koala.core.dtos.job.response.UsersMeJobsPostResponseDto;
import com.exskylab.koala.entities.Job;

public interface JobService {
    UsersMeJobsPostResponseDto createIndividualJob(UsersMeJobsPostRequestDto usersMeJobsPostRequestDto);

    CompaniesCompanyIdJobsPostResponseDto createCorporateJob(String companyId, CompaniesCompanyIdJobsPostRequestDto companiesCompanyIdJobsPostRequestDto);

    Job getById(String jobId);

    boolean existsById(String jobId);

}
