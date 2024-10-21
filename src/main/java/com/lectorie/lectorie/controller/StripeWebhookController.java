package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.exception.custom.UserNotFoundException;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.repository.TutorRepository;
import com.lectorie.lectorie.repository.UserRepository;
import com.lectorie.lectorie.service.EnrollmentService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
public class StripeWebhookController {

    @Value("${stripe.webhook.key}")
    private String webhookSecret;

    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;

    public StripeWebhookController(EnrollmentService enrollmentService, UserRepository userRepository) {
        this.enrollmentService = enrollmentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            String endpointSecret = webhookSecret;
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            System.out.println("event type = " + event.getType());
            if ("checkout.session.completed".equals(event.getType())) {

                Session session = (Session) event.getData().getObject();
                if ("paid".equals(session.getPaymentStatus())) {

                    // Retrieve custom metadata for booking and enrollment
                    String tutorId = session.getMetadata().get("tutorId");
                    String userId = session.getMetadata().get("userId");
                    String duration = session.getMetadata().get("duration");
                    String startTime = session.getMetadata().get("startTime");
                    String price = session.getMetadata().get("price");

                    // Proceed to create enrollment and booking
                    enrollmentService.createEnrollmentAndBooking(userId, tutorId, ZonedDateTime.parse(startTime), BookingDuration.valueOf(duration), Double.parseDouble(price));
                }
            }

            if ("checkout.session.completed".equals(event.getType())
                    || "checkout.session.expired".equals(event.getType())
                    || "payment_intent.payment_failed".equals(event.getType())
                    || "payment_intent.canceled".equals(event.getType())
            ) {
                Session session = (Session) event.getData().getObject();

                String userId = session.getMetadata().get("userId");

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("user not found", 3990));



                userRepository.save(
                        user.changeSession(null)
                );
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}