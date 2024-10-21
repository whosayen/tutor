package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, String>, JpaSpecificationExecutor<Tutor> {


}
