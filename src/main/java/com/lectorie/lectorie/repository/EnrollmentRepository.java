package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.Booking;
import com.lectorie.lectorie.model.Enrollment;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    @Query("SELECT b FROM Booking b WHERE b.enrollment = :enrollment AND b.time BETWEEN :startTime AND :endTime")
    List<Booking> findBookingsWithinPeriod(
            @Param("enrollment") Enrollment enrollment,
            @Param("startTime") ZonedDateTime startTime,
            @Param("endTime") ZonedDateTime endTime
    );


    Optional<Enrollment> findEnrollmentsByUserSettingsAndTutor(UserSettings userSettings, Tutor tutor);

    List<Enrollment> findEnrollmentsByTutorId(String tutorId);
}
