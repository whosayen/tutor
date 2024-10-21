package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.response.PaymentResponse;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.exception.custom.BookingException;
import com.lectorie.lectorie.exception.custom.InsufficientBalanceException;
import com.lectorie.lectorie.exception.custom.StripeAccountNotFoundException;
import com.lectorie.lectorie.exception.custom.UserNotFoundException;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.repository.TutorRepository;
import com.lectorie.lectorie.repository.UserRepository;
import com.lectorie.lectorie.util.UserUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.TransferCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class PaymentService {

    private final UserUtil userUtil;
    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;

    @Value("${stripe.commission}")
    private double stripeCommission;

    @Value("${frontend.url}")
    private String frontendUrl;



    public PaymentService(UserUtil userUtil, TutorRepository tutorRepository, UserRepository userRepository) {
        this.userUtil = userUtil;
        this.tutorRepository = tutorRepository;
        this.userRepository = userRepository;
    }

    public PaymentResponse createPaymentLink(String userId,
                                             String tutorId,
                                             ZonedDateTime startTime,
                                             String languageToTeach,
                                             double price,
                                             BookingDuration bookingDuration,
                                             double hourlyRate) throws StripeException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found", 3990));

        if (user.getSession() != null) {
            Session session =
                    Session.retrieve(
                            user.getSession()
                    );

            session.expire();
        }

        double originalPrice = hourlyRate * bookingDuration.getDurationMinutes() / 60 * 100;

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount((long) price)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(languageToTeach)
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .setQuantity(1L)
                                        .build()
                                )
                        .setExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond())
                        .putMetadata("userId", userId)
                        .putMetadata("tutorId", tutorId)
                        .putMetadata("duration", bookingDuration.toString())
                        .putMetadata("startTime", startTime.toString())
                        .putMetadata("price", String.valueOf(originalPrice))
                        .setSuccessUrl(frontendUrl + "/success")
                        .setCancelUrl(frontendUrl + "/failure")
                        .build();

        Session session = Session.create(params);
        userRepository.save(
                user.changeSession(session.getId())
        );


        return new PaymentResponse(session.getUrl(), session.getPaymentIntent());
    }

    public Account createConnectedAccount(Principal currentUser) throws StripeException {
        User user = userUtil.extractUser(currentUser);

        Tutor tutor = user.getTutor();

        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setEmail(user.getEmail())
                        .setController(
                                AccountCreateParams.Controller.builder()
                                        .setLosses(
                                                AccountCreateParams.Controller.Losses.builder()
                                                        .setPayments(AccountCreateParams.Controller.Losses.Payments.APPLICATION)
                                                        .build()
                                                )
                                        .setFees(
                                                AccountCreateParams.Controller.Fees.builder()
                                                        .setPayer(AccountCreateParams.Controller.Fees.Payer.APPLICATION)
                                                        .build()
                                                )
                                        .setStripeDashboard(
                                                AccountCreateParams.Controller.StripeDashboard.builder()
                                                        .setType(AccountCreateParams.Controller.StripeDashboard.Type.EXPRESS)
                                                        .build()
                                                )
                                        .build()
                                )
                        .build();

        Account account = Account.create(params);

        tutorRepository.save(
                tutor.changeConnectedAccountId(account.getId())
        );

        return account;
    }

    public String createAccountLink(Principal currentUser) throws StripeException {
        User user = userUtil.extractUser(currentUser);

        Tutor tutor = user.getTutor();

        String connnecedAccountId;
        if (Objects.requireNonNull(tutor).getConnectedAccountId() == null) {
            connnecedAccountId = createConnectedAccount(currentUser).getId();
        } else {
            connnecedAccountId = tutor.getConnectedAccountId();
        }

        AccountLinkCreateParams params =
                AccountLinkCreateParams.builder()
                        .setAccount(connnecedAccountId)
                        .setReturnUrl(frontendUrl + "/wallet")
                        .setRefreshUrl(frontendUrl + "/wallet")  // Add this line
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();
        AccountLink accountLink = AccountLink.create(params);

        return accountLink.getUrl();
    }

    @Transactional
    public String getPaymentFromWallet(Principal currentUser, long amount) throws StripeException {
        if (amount < 5000) {
            throw new InsufficientBalanceException("cant get money less than 50 dollars",3990);
        }

        User user = userUtil.extractUser(currentUser);
        String StripeAccountId = user.getTutor().getConnectedAccountId();
        if (StripeAccountId == null) {
            throw new StripeAccountNotFoundException("Create stripe account from your page",3990);
        }

        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException("you don't have enough money",3990);
        }

        TransferCreateParams params =
                TransferCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("usd")
                        .setDestination(StripeAccountId)
                        .build();

        Transfer.create(params);

        userRepository.save(
                user.changeBalance(user.getBalance() - amount)
        );

        return "transfer has been started";
    }
}
