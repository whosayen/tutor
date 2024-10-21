package com.lectorie.lectorie.controller;

import com.lectorie.lectorie.dto.*;
import com.lectorie.lectorie.dto.request.ChangeSettingsRequest;
import com.lectorie.lectorie.dto.request.LoginRequest;
import com.lectorie.lectorie.dto.request.RegistrationRequest;
import com.lectorie.lectorie.dto.response.LoginResponse;
import com.lectorie.lectorie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> createUser(@RequestBody @Valid RegistrationRequest userRequest
                                                             ) throws IOException {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request,response);
    }

    @GetMapping("/find/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<FullUserDto>> waitingApprove() {
        return ResponseEntity.ok(userService.waitingApprove());
    }

    @PutMapping("/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TutorDto> approve(@RequestParam String email, @RequestParam Boolean isApproved) {
        return ResponseEntity.ok(userService.approve(email, isApproved));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FullUserDto> getUser(Principal currentUser, @PathVariable String userId) throws DataFormatException, IOException {
        return ResponseEntity.ok(userService.getUserFromId(currentUser, userId));
    }

    @GetMapping("/public/{userId}")
    public ResponseEntity<FullUserSearchDto> getUser(@PathVariable String userId) throws DataFormatException, IOException {
        return ResponseEntity.ok(userService.getSearchUserFromId(userId));
    }

    @PutMapping("/change")
    public ResponseEntity<FullUserDto> changeSettings(Principal currentUser, @RequestBody ChangeSettingsRequest changeSettingsRequest) throws DataFormatException, IOException {
        return ResponseEntity.ok(userService.changeSettings(currentUser, changeSettingsRequest));
    }


}