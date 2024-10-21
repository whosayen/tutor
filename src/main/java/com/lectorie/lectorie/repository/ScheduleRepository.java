package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
