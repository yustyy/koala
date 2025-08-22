package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.PhoneVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneVerificationDao extends JpaRepository<PhoneVerification, UUID> {
}
