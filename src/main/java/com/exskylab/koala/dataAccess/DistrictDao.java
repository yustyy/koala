package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.City;
import com.exskylab.koala.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DistrictDao extends JpaRepository<District, UUID> {
    List<District> findDistrictsByCityIdOrderByNameAsc(UUID cityId);

    Optional<District> findByNameAndCityAllIgnoreCase(String name, City city);
}
