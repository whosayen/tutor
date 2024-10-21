package com.lectorie.lectorie.repository;


import com.lectorie.lectorie.enums.Status;
import com.lectorie.lectorie.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByStatusAndTimeBefore(Status status, ZonedDateTime time);

}
