package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.CompanyContactService;
import com.exskylab.koala.business.abstracts.JobApplicationService;
import com.exskylab.koala.business.abstracts.JobService;
import com.exskylab.koala.business.abstracts.SecurityService;
import com.exskylab.koala.core.constants.JobApplicationMessages;
import com.exskylab.koala.core.dtos.jobApplication.request.JobsJobIdApplicationsRequestDto;
import com.exskylab.koala.dataAccess.JobApplicationDao;
import com.exskylab.koala.entities.ApplicationStatus;
import com.exskylab.koala.entities.JobApplication;
import com.exskylab.koala.entities.JobEmployerType;
import com.exskylab.koala.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class JobApplicationManager implements JobApplicationService {

    private final JobApplicationDao jobApplicationDao;
    private final JobService jobService;
    private final CompanyContactService companyContactService;
    private final SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(JobApplicationManager.class);


    public JobApplicationManager(JobApplicationDao jobApplicationDao, JobService jobService, SecurityService securityService, CompanyContactService companyContactService) {
        this.jobApplicationDao = jobApplicationDao;
        this.jobService = jobService;
        this.securityService = securityService;
        this.companyContactService = companyContactService;
    }

    @Override
    @Transactional
    public JobApplication applyToJob(String jobId, JobsJobIdApplicationsRequestDto jobsJobIdApplicationsRequestDto) {
        logger.info("Applying to job with id: {}", jobId);
        var job = jobService.getById(jobId);

        User authenticatedUser = securityService.getAuthenticatedUserFromContext();
        logger.info("Applying to job with id: {} for userId: {}", jobId, authenticatedUser.getId());

        if (job.getCreator().getId().equals(authenticatedUser.getId())) {
            throw new IllegalArgumentException(JobApplicationMessages.USER_CANNOT_APPLY_TO_OWN_JOB);
        }

        if (job.getEmployerType().equals(JobEmployerType.COMPANY)){
            if (companyContactService.isUserAContactOfCompany(job.getCompany().getId(), authenticatedUser.getId())) {
                throw new IllegalArgumentException(JobApplicationMessages.CONTACT_USER_CANNOT_APPLY_TO_COMPANY_JOB);
            }

            if (jobApplicationDao.existsByJobIdAndUserId(UUID.fromString(jobId), authenticatedUser.getId())) {
                throw new IllegalArgumentException(JobApplicationMessages.USER_ALREADY_APPLIED_TO_JOB);
            }
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(authenticatedUser);
        jobApplication.setNotes(jobsJobIdApplicationsRequestDto.getNotes());
        jobApplication.setStatus(ApplicationStatus.APPLIED);
        jobApplication.setApplicationDateTime(LocalDateTime.now());
        jobApplication.setAssignment(null);

        JobApplication savedJobApplication = jobApplicationDao.save(jobApplication);
        logger.info("Saved job application with id: {}", jobApplication.getId());

        return savedJobApplication;

    }
}
