package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PendingRegistrationDao extends JpaRepository<PendingRegistration, UUID> {
    Optional<PendingRegistration> findByEmail(String email);

    Optional<PendingRegistration> findByToken(UUID token);
}
