package com.exskylab.koala.dataAccess;

import com.exskylab.koala.entities.City;
import com.exskylab.koala.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CityDao extends JpaRepository<City, UUID> {
    List<City> findCitiesByCountryIdOrderByNameAsc(UUID countryId);

    Optional<City> findByNameAndCountryAllIgnoreCase(String name, Country country);
}
