package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.JobAssignment;

public interface JobAssignmentService {
    JobAssignment createJobAssignmentFromApplication(JobAssignment jobAssignment);
}
