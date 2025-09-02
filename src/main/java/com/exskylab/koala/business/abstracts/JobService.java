package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.job.request.CompaniesCompanyIdJobsPostRequestDto;
import com.exskylab.koala.core.dtos.job.request.UsersMeJobsPostRequestDto;
import com.exskylab.koala.entities.Job;

import java.util.UUID;

public interface JobService {
    Job createIndividualJob(UsersMeJobsPostRequestDto usersMeJobsPostRequestDto);

    Job createCorporateJob(String companyId, CompaniesCompanyIdJobsPostRequestDto companiesCompanyIdJobsPostRequestDto);

    Job getById(String jobId);
}
