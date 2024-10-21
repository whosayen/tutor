package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/account")
    public ResponseEntity<Account> createConnectedAccount(Principal currentUser) throws StripeException {
        return ResponseEntity.ok(paymentService.createConnectedAccount(currentUser));
    }

    @PostMapping("/link")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<String> createAccountLink(Principal currentUser) throws StripeException {
        return ResponseEntity.ok(paymentService.createAccountLink(currentUser));
    }

    @PostMapping("/transfer-money")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<String> getPaymentFromWallet(Principal currentUser, @RequestParam long amount) throws StripeException {
        return ResponseEntity.ok(paymentService.getPaymentFromWallet(currentUser, amount));
    }
}
