package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.dto.*;
import com.lectorie.lectorie.dto.request.ChangeUserSettingsRequest;
import com.lectorie.lectorie.model.User;
import com.lectorie.lectorie.service.UserSettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.zip.DataFormatException;

import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1/profile")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @PutMapping("/picture")
    public ResponseEntity<UserSettingsDto> uploadProfilePicture(@RequestParam("image") MultipartFile file, Principal currentUser) throws IOException, DataFormatException {
        return new ResponseEntity<>(userSettingsService.uploadProfilePicture(file, currentUser), HttpStatus.OK);
    }

    @GetMapping("/picture")
    public ResponseEntity<byte[]> downloadProfilePicture(Principal currentUser) {
        byte[] imageData = userSettingsService.downloadProfilePicture(currentUser);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(IMAGE_PNG_VALUE))
                .body(imageData);
    }

    @PutMapping("/change")
    public ResponseEntity<UserSettingsDto> changeUserSettings(Principal currentUser,@RequestBody ChangeUserSettingsRequest changeUserSettingsRequest) throws DataFormatException, IOException {
        return ResponseEntity.ok(userSettingsService.changeUserSettings(currentUser, changeUserSettingsRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto>  getUserInfo(Principal currentUser) {
        return ResponseEntity.ok(userSettingsService.getUser(currentUser));
    }

    @GetMapping("/user-settings")
    public ResponseEntity<UserSettingsDto> getUserSettingsInfo(Principal currentUser) throws DataFormatException, IOException {
        return ResponseEntity.ok(userSettingsService.getUserSettings(currentUser));
    }

    @GetMapping("/tutor")
    public ResponseEntity<TutorDto> getTutorInfo(Principal currentUser) {
        return ResponseEntity.ok(userSettingsService.getTutor(currentUser));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingDto>> getBookings(Principal currentUser) {
        return ResponseEntity.ok(userSettingsService.getBookings(currentUser));
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<List<EnrollmentDto>> getEnrollments(Principal currentUser) {
        return ResponseEntity.ok(userSettingsService.getEnrollments(currentUser));
    }
}