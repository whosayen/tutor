package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findByName(String name);
    boolean existsByName(String name);
}
