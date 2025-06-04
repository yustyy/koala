package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.User;
import com.exskylab.koala.entities.Verification;
import com.exskylab.koala.entities.VerificationStatus;
import com.exskylab.koala.entities.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationDao extends JpaRepository<Verification, UUID> {

    Optional<Verification> findByToken(String token);

    List<Verification> findByUserAndVerificationType(User user, VerificationType verificationType);

    List<Verification> findByUser_IdAndVerificationType(UUID userId, VerificationType verificationType);

    List<Verification> findByUser_IdAndVerificationTypeAndStatus(UUID userId, VerificationType verificationType, VerificationStatus status);

    List<Verification> findByUser_IdAndVerificationTypeAndStatusOrderByCreatedAtDesc(UUID userId, VerificationType verificationType, VerificationStatus status);


}
