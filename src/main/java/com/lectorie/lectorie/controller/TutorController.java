package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.dto.*;
import com.lectorie.lectorie.dto.request.*;
import com.lectorie.lectorie.model.Language;
import com.lectorie.lectorie.enums.Level;
import com.lectorie.lectorie.enums.Status;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.repository.LanguageRepository;
import com.lectorie.lectorie.service.TutorService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/v1/tutor")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PutMapping("/change-subject-taught")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<TutorDto> changeTutorSubjectTaught(@RequestBody LanguageDto subjectTaughtRequest, Principal currentUser) {
        return ResponseEntity.ok(tutorService.changeTutorSubjectTaught(subjectTaughtRequest, currentUser));
    }

    @PutMapping("/change-spoken-language")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<TutorDto> changeTutorSpokenLanguages(@RequestBody ChangeTutorSpokenLanguagesRequest changeTutorSpokenLanguagesRequest, Principal currentUser) {
        return ResponseEntity.ok(tutorService.changeTutorSpokenLanguages(changeTutorSpokenLanguagesRequest, currentUser));
    }

    @PutMapping("/change-hourly-rate")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<TutorDto> changeHourlyRate(@RequestBody HourlyRateRequest hourlyRateRequest, Principal currentUser) {
        return ResponseEntity.ok(tutorService.changeHourlyRate(hourlyRateRequest, currentUser));
    }

    @PutMapping("/change-description")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<TutorDto> changeProfileDescriptionSettings(@RequestBody DescriptionSettingsRequest descriptionSettingsRequest, Principal currentUser) {
        return ResponseEntity.ok(tutorService.changeTutorDescriptionSettings(descriptionSettingsRequest, currentUser));
    }

    @PutMapping("/change-video")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<TutorDto> changeVideoSettings(@RequestBody VideoSettingsRequest videoSettingsRequest, Principal currentUser) {
        return ResponseEntity.ok(tutorService.changeVideoSettings(videoSettingsRequest, currentUser));
    }

    @PutMapping("/change-availability")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<TutorDto> changeProfileAvailabilitySettings(@RequestBody ScheduleSettingsRequest scheduleSettingsRequest, Principal currentUser) {
        return ResponseEntity.ok(tutorService.changeScheduleSettings(scheduleSettingsRequest, currentUser));
    }

    @GetMapping("/check-approved")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<Boolean> checkApproved(Principal currentUser) {
        return ResponseEntity.ok(tutorService.checkApproved(currentUser));
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDto>> getBookings(Principal currentUser,
                                                        @RequestParam ZonedDateTime startTime,
                                                        @RequestParam ZonedDateTime finishTime,
                                                        @RequestParam(required = false) Status status) {
        return ResponseEntity.ok(tutorService.getBookings(currentUser, startTime, finishTime, status));
    }

    @PutMapping("/bookings/{bookingId}")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<BookingDto> answerBooking(Principal currentUser, @PathVariable String bookingId, @RequestParam boolean accepted) {
        return ResponseEntity.ok(tutorService.answerBookingRequest(currentUser, bookingId, accepted));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FullUserSearchDto>> searchTutors(
            @RequestParam(required = false) Level languageLevel,
            @RequestParam(required = false) String knownLanguage,
            @RequestParam(required = false) String languageToTeach,
            @RequestParam(required = false) Double minRate,
            @RequestParam(required = false) Double maxRate,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {


        return ResponseEntity.ok(tutorService.searchTutor(languageLevel, knownLanguage, languageToTeach, minRate, maxRate, keyword, page, size, sortBy));
    }
}
