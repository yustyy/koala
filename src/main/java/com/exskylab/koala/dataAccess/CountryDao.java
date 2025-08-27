package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryDao extends JpaRepository<Country, UUID> {

    List<Country> findCountriesByOrderByNameAsc();

    Optional<Country> findByCodeAndNameAllIgnoreCase(String code, String name);

}
