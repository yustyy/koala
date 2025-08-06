package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.IdentityVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdentityVerificationDao extends JpaRepository<IdentityVerification, UUID> {
}
