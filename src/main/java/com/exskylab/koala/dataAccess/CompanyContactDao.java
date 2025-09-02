package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.CompanyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyContactDao extends JpaRepository<CompanyContact, UUID> {
    boolean existsByCompanyIdAndUserId(UUID companyId, UUID userId);
}
