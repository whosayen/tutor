package com.lectorie.lectorie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lectorie.lectorie.dto.*;
import com.lectorie.lectorie.dto.request.*;
import com.lectorie.lectorie.dto.response.LoginResponse;
import com.lectorie.lectorie.exception.custom.*;
import com.lectorie.lectorie.model.*;
import com.lectorie.lectorie.enums.Role;
import com.lectorie.lectorie.enums.TokenType;
import com.lectorie.lectorie.repository.*;
import com.lectorie.lectorie.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository; //not recommended but okey
    private final UserSettingsRepository userSettingsRepository;
    private final TutorService tutorService;
    private final TutorRepository tutorRepository;
    private final UserUtil userUtil;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;


    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenService tokenService, EmailVerificationTokenRepository emailVerificationTokenRepository, UserSettingsRepository userSettingsRepository, TutorService tutorService, TutorRepository tutorRepository, UserUtil userUtil, UserRepository userRepository, CountryRepository countryRepository, LanguageRepository languageRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.tutorService = tutorService;
        this.tutorRepository = tutorRepository;
        this.userUtil = userUtil;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.languageRepository = languageRepository;
    }

    public LoginResponse createUser(RegistrationRequest registrationRequest) {
        EmailVerificationToken token = emailVerificationTokenRepository.findByEmail(registrationRequest.email())
                .orElseThrow(() -> new NoSuchEmailVerificationTokenException("there is no email verification token with this email", 4043));

        if (!token.isEnabled()) {
            throw new EmailIsNotVerifiedException("this email is not verified", 4002);
        }

        UserSettings userSettings = new UserSettings( // TODO DATE OF BIRTH ZORUNLU DEGIL ISE SIL
                registrationRequest.firstName(),
                registrationRequest.lastName(),
                registrationRequest.country(),
                ZoneId.of(registrationRequest.timezone()),
                registrationRequest.dateOfBirth()
        );


        User user = new User(
                registrationRequest.email(),
                passwordEncoder.encode(registrationRequest.password()),
                registrationRequest.role(),
                userSettingsRepository.save(userSettings),
                (registrationRequest.role() == Role.TUTOR) ? tutorService.createTutor() : null,
                true,
                0,
                null
        );

        emailVerificationTokenRepository.delete(token);
        var savedUser = repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(jwtToken, savedUser);

        return new LoginResponse(jwtToken, refreshToken, registrationRequest.role(), savedUser.getId());
    }
    protected boolean checkEmailExist(String email) {
        return repository.existsByEmail(email);
    }


    private void saveUserToken(String jwtToken, User savedUser) {
        var token = new Token(
                jwtToken,
                TokenType.BEARER,
                false,
                false,
                savedUser);

        tokenService.addToken(token);
    }

    public LoginResponse login(LoginRequest loginRequest) {

        User user = repository.findByEmail(loginRequest.email())
                .filter(u -> passwordEncoder.matches(loginRequest.password(), u.getPassword()))
                .orElseThrow(() -> new UserNotFoundException("invalid email or password", 4041));


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(jwtToken, user);
        return new LoginResponse(jwtToken, refreshToken, user.getRole(), user.getId());
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenService.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenService.addAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("user not found", 4041));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(accessToken, user);
                var authResponse = new LoginResponse(accessToken, refreshToken, user.getRole(), user.getId());
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public TutorDto approve(String email, Boolean isApproved) {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("there are no users registered with this email", 4041));

        if (user.getRole() != Role.TUTOR)
            throw new ValidationErrorException("user is not tutor", 3997);

        return tutorService.approve(user, isApproved);
    }

    public FullUserDto getUserFromId(Principal currentUser, String userId) throws DataFormatException, IOException {
        User user = userUtil.extractUser(currentUser);
        if (!user.getId().equals(userId)) {
            throw new ValidationErrorException("you cant see the profile page", 3997);
        }

        return FullUserDto.convert(repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found with this id", 9999)));
    }

    public FullUserSearchDto getSearchUserFromId(String userId) throws DataFormatException, IOException {
        return FullUserSearchDto.convert(repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found with this id", 9999)));

    }

    public FullUserDto changeSettings(Principal currentUser, ChangeSettingsRequest changeSettingsRequest) throws DataFormatException, IOException {
        User user = userUtil.extractUser(currentUser);
        UserSettings userSettings = user.getUserSettings();
        Tutor tutor = user.getTutor() != null ? user.getTutor() : null;

        ChangeTutorSettingsRequest changeTutorSettingsRequest = changeSettingsRequest.changeTutorSettingsRequest();
        ChangeUserSettingsRequest changeUserSettingsRequest = changeSettingsRequest.changeUserSettingsRequest();
        ChangeUserRequest changeUserRequest = changeSettingsRequest.changeUserRequest();


        if (changeUserSettingsRequest != null) {
            UserSettings newUserSettings = userSettings
                    .changeFirstName(changeUserSettingsRequest.firstName() != null ? changeUserSettingsRequest.firstName() : userSettings.getFirstName())
                    .changeLastName(changeUserSettingsRequest.lastName() != null ? changeUserSettingsRequest.lastName() : userSettings.getLastName())
                    .changeCountry(changeUserSettingsRequest.countryName() != null ? countryRepository.findByName(changeUserSettingsRequest.countryName())
                            .orElseThrow(() -> new CountryNotFoundException("country not found",3990)) : userSettings.getCountry())
                    .changeCity(changeUserSettingsRequest.city() != null ? changeUserSettingsRequest.city() : userSettings.getCity())
                    .changePhoneCode(changeUserSettingsRequest.phoneCode() != null ? changeUserSettingsRequest.phoneCode() : userSettings.getPhoneCode())
                    .changePhoneNumber(changeUserSettingsRequest.phoneNumber() != null ? changeUserSettingsRequest.phoneNumber() : userSettings.getPhoneNumber())
                    .changeTimezone(changeUserSettingsRequest.timezone() != null ? ZoneId.of(changeUserSettingsRequest.timezone()) : userSettings.getTimezone())
                    .changeDateOfBirth(changeUserSettingsRequest.dateOfBirth() != null ? changeUserSettingsRequest.dateOfBirth() : userSettings.getDateOfBirth());


            userSettings = userSettingsRepository.save(newUserSettings);
        }

        if (tutor != null && changeTutorSettingsRequest != null) {
            Tutor newTutor = tutor
                    .changeLanguageToTeach(changeTutorSettingsRequest.languageDto() != null ? languageRepository.findById(changeTutorSettingsRequest.languageDto().languageName()).orElseThrow(
                            () -> new LanguageDoesNotExistException("language cant found",3990)) : tutor.getLanguageToTeach())
                    .changeHourlyRate(changeTutorSettingsRequest.hourlyRate() != null ? changeTutorSettingsRequest.hourlyRate() : tutor.getHourlyRate())
                    .changeShortDescription(changeTutorSettingsRequest.shortDescription() != null ? changeTutorSettingsRequest.shortDescription() : tutor.getShortDescription())
                    .changeDescription(changeTutorSettingsRequest.description() != null ? changeTutorSettingsRequest.description() : tutor.getDescription())
                    .changeVideoUrl(changeTutorSettingsRequest.videoUrl() != null ? changeTutorSettingsRequest.videoUrl() : tutor.getVideoUrl());

            tutor = tutorRepository.save(newTutor);
        }

        if (changeUserRequest != null) {
            user = user
                    .changePassword(changeUserRequest.password() != null ? passwordEncoder.encode(user.getPassword()) : user.getPassword());
        }

        user = userRepository.save(user
                        .changeUserSettings(userSettings)
                        .changeTutor(tutor)
                );

        return FullUserDto.convert(user);
    }


    public List<FullUserDto> waitingApprove() {
        return repository.findByRoleAndTutorIsApproved(Role.TUTOR, false)
                .stream()
                .map(user -> {
                    try {
                        return FullUserDto.convert(user);
                    } catch (DataFormatException | IOException e) {
                        // Handle the exception as needed, e.g., logging or returning a default value
                        throw new RuntimeException("Error converting user to FullUserDto", e);
                    }
                })
                .toList();
    }
}
