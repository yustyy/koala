package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.JobAssignmentService;
import com.exskylab.koala.dataAccess.JobAssignmentDao;
import com.exskylab.koala.entities.JobAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobAssigmentManager implements JobAssignmentService {

    private final JobAssignmentDao jobAssignmentDao;

    private final Logger logger = LoggerFactory.getLogger(JobAssigmentManager.class);


    public JobAssigmentManager(JobAssignmentDao jobAssignmentDao) {
        this.jobAssignmentDao = jobAssignmentDao;
    }


    @Override
    public JobAssignment createJobAssignmentFromApplication(JobAssignment jobAssignment) {
        logger.info("Creating job assignment from application...");
        return jobAssignmentDao.save(jobAssignment);
    }
}
