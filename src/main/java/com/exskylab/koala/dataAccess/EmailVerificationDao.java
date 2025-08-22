package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailVerificationDao extends JpaRepository<EmailVerification, UUID> {
    boolean existsByNewEmailAndVerifiedFalse(String newEmail);

    void deleteByNewEmail(String newEmail);

}
