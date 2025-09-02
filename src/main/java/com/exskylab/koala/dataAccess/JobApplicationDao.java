package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobApplicationDao extends JpaRepository<JobApplication, UUID> {

    boolean existsByJobIdAndUserId(UUID jobId, UUID userId);

    List<JobApplication> findAllByJobId(UUID jobId);
}
