package com.lectorie.lectorie.exception;

import com.lectorie.lectorie.dto.response.ErrorResponse;
import com.lectorie.lectorie.exception.custom.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**

 * 4001 = Email Verification token gönderirken email eğer db de yoksa gönderilir.gönderilirtoken suresi
 *      bittigi zaman gonderilir, eger bu gonderildiyse emaile yeni bir token gonderilmistir.
 * 4002 = user yaratırken email verified değilse
 * 4041 = user not found in database
 * 4042 = checkOtp fonskiyonunda gonderilen emaile ait token yoktur
 * 4043 = createUser fonksiyonunda gonderilen emaile ait token yoktur
 * 4011 = tutor registration kısmını bitirmeden bi yerlere girmek isterse
 * 4044 Booking not found
 * 3990 Custom Exceptions
 *
 *
 * 999 = Sistem atti valla bizle alakali degil
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /*
     * CUSTOM EXCEPTIONS
     * */

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(ValidationErrorException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TutorRegistrationIncompleteException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(TutorRegistrationIncompleteException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(EmailAlreadyInUseException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoSuchEmailVerificationTokenException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(NoSuchEmailVerificationTokenException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailVerificationTokenExpiredException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(EmailVerificationTokenExpiredException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongOtpException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(WrongOtpException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailIsNotVerifiedException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(EmailIsNotVerifiedException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageDoesNotExistException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(ImageDoesNotExistException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(BookingNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(BookingException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(ChatRoomNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(CountryNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CountryAlreadyExistException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(CountryAlreadyExistException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(ImageException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(InsufficientBalanceException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LanguageAlreadyExistException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(LanguageAlreadyExistException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LanguageDoesNotExistException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(LanguageDoesNotExistException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(LessonNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(MessageException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(ScheduleNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StripeAccountNotFoundException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handleNotFoundException(StripeAccountNotFoundException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /*
     * CUSTOM EXCEPTIONS
     * */


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ErrorResponse>>> handle(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorResponse(
                        999,
                        fieldError.getDefaultMessage()
                        ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // General Exception Handlers
    //@ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, List<ErrorResponse>>> handleGeneralExceptions(Exception ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Map<String, List<ErrorResponse>>> handleRuntimeExceptions(RuntimeException ex) {
        return new ResponseEntity<>(getErrorsMap(getErrorList(ex)), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Map<String, List<ErrorResponse>> getErrorsMap(List<ErrorResponse> errors) {
        Map<String, List<ErrorResponse>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    private List<ErrorResponse> getErrorList(Exception ex) {
        int code = ex instanceof IHasCode ? ((IHasCode) ex).getCode() : 999;

        return Collections.singletonList(
                new ErrorResponse(
                        code,
                        ex.getMessage())
        );
    }
}
