package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.enums.Role;
import com.lectorie.lectorie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isAccountNonLocked = true AND u.tutor.isApproved = :isApproved")
    List<User> findByRoleAndTutorIsApproved(@Param("role") Role role, @Param("isApproved") boolean isApproved);
}
