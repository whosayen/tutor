package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.LanguageLevel;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


public interface LanguageLevelRepository extends JpaRepository<LanguageLevel, Long> {

}
