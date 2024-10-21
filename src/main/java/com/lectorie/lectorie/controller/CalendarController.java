package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.dto.CalendarDto;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.service.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/{userId}/{weekLater}/{bookingDuration}")
    public ResponseEntity<CalendarDto> getCalender(Principal currentUser, @PathVariable String userId, @PathVariable int weekLater, @PathVariable BookingDuration bookingDuration) {
        return ResponseEntity.ok(calendarService.getCalender(currentUser, weekLater, userId, bookingDuration));
    }

    @GetMapping("/booking-durations/{tutorId}")
    public ResponseEntity<List<BookingDuration>> getBookingDurations(@PathVariable String tutorId) {
        return ResponseEntity.ok(calendarService.getBookingDurations(tutorId));
    }
}
