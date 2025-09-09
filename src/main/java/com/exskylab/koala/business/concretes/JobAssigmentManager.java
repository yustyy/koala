package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.JobAssignmentService;
import com.exskylab.koala.business.abstracts.SecurityService;
import com.exskylab.koala.core.constants.JobAssignmentMessages;
import com.exskylab.koala.core.dtos.jobAssignment.request.JobAssignmentsAssignmentIdStatusPatchRequestDto;
import com.exskylab.koala.core.dtos.jobAssignment.response.JobAssignmentsAssignmentIdStatusPatchResponseDto;
import com.exskylab.koala.core.exceptions.AssignmentAlreadyAnsweredException;
import com.exskylab.koala.core.exceptions.ResourceNotFoundException;
import com.exskylab.koala.core.exceptions.UserNotWorkerForAssignmentException;
import com.exskylab.koala.core.mappers.JobAssignmentMapper;
import com.exskylab.koala.dataAccess.JobAssignmentDao;
import com.exskylab.koala.entities.AssignmentStatus;
import com.exskylab.koala.entities.JobAssignment;
import com.exskylab.koala.entities.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobAssigmentManager implements JobAssignmentService {

    private final JobAssignmentDao jobAssignmentDao;

    private final Logger logger = LoggerFactory.getLogger(JobAssigmentManager.class);
    private final SecurityService securityService;
    private final JobAssignmentMapper jobAssignmentMapper;


    public JobAssigmentManager(JobAssignmentDao jobAssignmentDao, SecurityService securityService, JobAssignmentMapper jobAssignmentMapper) {
        this.jobAssignmentDao = jobAssignmentDao;
        this.securityService = securityService;
        this.jobAssignmentMapper = jobAssignmentMapper;
    }


    @Override
    public JobAssignment save(JobAssignment jobAssignment) {
        logger.info("Creating job assignment from application...");
        return jobAssignmentDao.save(jobAssignment);
    }

    @Override
    public JobAssignmentsAssignmentIdStatusPatchResponseDto answerToAssignment(UUID assignmentId, JobAssignmentsAssignmentIdStatusPatchRequestDto jobAssignmentsAssignmentIdStatusPatchRequestDto) {
        logger.info("Answering to job assignment from");
        var assignment = jobAssignmentDao.findById(assignmentId).orElseThrow(() -> {
            logger.info("Assignment with id {} not found", assignmentId);
            return new ResourceNotFoundException(JobAssignmentMessages.NOT_FOUND);
        });

        logger.info("Job assignment with id {} found", assignmentId);

        if (assignment.getStatus() != AssignmentStatus.PENDING_CONFIRMATION){
            logger.warn("Assignment with ID: {} is already answered.", assignment.getId());
            throw new AssignmentAlreadyAnsweredException(JobAssignmentMessages.ALREADY_ANSWERED);
        }

        var authenticatedUserFromContext = securityService.getAuthenticatedUserFromContext();

        if (!assignment.getWorker().getId().equals(authenticatedUserFromContext.getId())) {
            throw new UserNotWorkerForAssignmentException(JobAssignmentMessages.NOT_WORKER_FOR_ASSIGNMENT);
        }

        if (jobAssignmentsAssignmentIdStatusPatchRequestDto.isAccepted()){
            assignment.setStatus(AssignmentStatus.CONFIRMED);
            assignment.setPaymentStatus(PaymentStatus.PENDING_COLLECTION);
            logger.info("Job assignment with id {} has been accepted", assignmentId);
        }else{
            assignment.setStatus(AssignmentStatus.REJECTED);
            logger.info("Job assignment with id {} has been rejected", assignmentId);
        }

        assignment = jobAssignmentDao.save(assignment);

        logger.info("Job assignment with id {} has been saved", assignmentId);

        return jobAssignmentMapper.toJobAssignmentsAssignmentIdStatusPatchResponseDto(assignment);

    }
}
