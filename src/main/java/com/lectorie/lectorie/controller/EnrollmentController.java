package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.dto.BookingDto;
import com.lectorie.lectorie.dto.request.BookingRequest;
import com.lectorie.lectorie.dto.request.CancelBookingRequest;
import com.lectorie.lectorie.dto.EnrollmentDto;
import com.lectorie.lectorie.dto.request.RescheduleBookingRequest;
import com.lectorie.lectorie.dto.response.PaymentResponse;
import com.lectorie.lectorie.service.EnrollmentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/v1/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PutMapping("/book")
    public ResponseEntity<PaymentResponse> book(Principal currentUser, @RequestBody BookingRequest bookingRequest) throws StripeException {
        return ResponseEntity.ok(enrollmentService.book(currentUser, bookingRequest));
    }

    @PutMapping("/finish-lesson")
    public ResponseEntity<BookingDto> finishBooking(Principal currentUser, @RequestParam String bookId, @RequestParam Boolean isApproved) {
        return ResponseEntity.ok(enrollmentService.finishBooking(currentUser, bookId, isApproved));
    }

    @DeleteMapping("/cancel-booking/{bookId}")
    public ResponseEntity<String> cancelBooking(Principal currentUser, @PathVariable String bookId) {
        return ResponseEntity.ok(enrollmentService.cancelBooking(currentUser, bookId));
    }

    @PutMapping("/reschedule-booking")
    public ResponseEntity<String> rescheduleBooking(Principal currentUser, @RequestBody RescheduleBookingRequest bookingDto) {
        return ResponseEntity.ok(enrollmentService.rescheduleBooking(currentUser, bookingDto));
    }

    /*
    @PutMapping("/tutor-reschedule")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<String> rescheduleTutorBooking(Principal currentUser, @RequestBody RescheduleBookingRequest bookingDto) {
        return ResponseEntity.ok(enrollmentService.rescheduleTutorBooking(currentUser, bookingDto));
    }
     */

    @PutMapping("/rate")
    public ResponseEntity<EnrollmentDto> rateBooking(Principal currentUser, @RequestParam String tutorId, @RequestParam Integer rate, @RequestParam String comment) throws DataFormatException, IOException {
        return ResponseEntity.ok(enrollmentService.rateAndComment(currentUser, tutorId, rate, comment));
    }
}
