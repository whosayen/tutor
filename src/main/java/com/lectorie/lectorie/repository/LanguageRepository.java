package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.Country;
import com.lectorie.lectorie.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, String> {

    boolean existsByName(String name);
}
