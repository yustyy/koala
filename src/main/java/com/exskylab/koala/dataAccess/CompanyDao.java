package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.Company;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyDao extends JpaRepository<Company, UUID> {

    @EntityGraph(attributePaths = {
            "contacts",
            "contacts.user",
            "contacts.user.profilePicture",
            "logo",
            "address"
    })
    Optional<Company> findById(UUID id);

}
