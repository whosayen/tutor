package com.lectorie.lectorie.service;

import com.lectorie.lectorie.exception.custom.*;
import com.lectorie.lectorie.model.EmailVerificationToken;
import com.lectorie.lectorie.repository.EmailVerificationTokenRepository;
import com.lectorie.lectorie.util.EmailUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailVerificationTokenService {

    @Value("${constants.expiration.min-time}")
    long expirationTime;

    @Value("${otp.fixed}")
    boolean otpFixed;


    private final EmailVerificationTokenRepository repository;
    private final UserService userService;
    private final EmailUtil emailUtil;

    public EmailVerificationTokenService(EmailVerificationTokenRepository repository, UserService userService, EmailUtil emailUtil) {
        this.repository = repository;
        this.userService = userService;
        this.emailUtil = emailUtil;
    }

    private EmailVerificationToken saveEmailVerificationToken(String email) {
        return repository.findByEmail(email)
                .map(emailVerificationToken -> repository.save(
                        new EmailVerificationToken(
                                emailVerificationToken.getId(),
                                email,
                                generateOtp(),
                                LocalDateTime.now().plusMinutes(expirationTime)
                        )
                )).orElseGet(() -> repository.save(
                        new EmailVerificationToken(
                                email,
                                generateOtp(),
                                LocalDateTime.now().plusMinutes(expirationTime)
                        )
                ));
    }


    public String sendEmailVerification(String email) {
        if (userService.checkEmailExist(email)) {
            throw new EmailAlreadyInUseException("YOU HAVE AN ACCOUNT", 4001);
        }

        EmailVerificationToken token = saveEmailVerificationToken(email);

        try {
            emailUtil.sendOtpEmail(token.getEmail(), token.getOtp());
        } catch (MessagingException e) {
            throw new MessageException("Unable to send otp please try again",3990);
        }
        return "email has been send";
    }

    public String checkOtp(String email, String otp) {
        EmailVerificationToken token = repository.findByEmail(email)
                .orElseThrow(() -> new NoSuchEmailVerificationTokenException("there is no token with this email: " + email, 4042));

        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            sendEmailVerification(email);
            throw new EmailVerificationTokenExpiredException("token expiration date has been passed, new token has been send to your email", 4003);
        } else if(!token.getOtp().equals(otp)) {
            throw new WrongOtpException("you have written wrong otp", 4004);
        }

        repository.save(
                token.changeIsEnabled(true)
        );

        return "token verified";
    }

    private String generateOtp() {
        if (otpFixed) {
            return "00000";
        }

        Random random = new Random();
        int randomNumber = random.nextInt(99999);
        StringBuilder output = new StringBuilder(Integer.toString(randomNumber));

        while (output.length() < 5) {
            output.insert(0, "0");
        }

        return output.toString();
    }
}
