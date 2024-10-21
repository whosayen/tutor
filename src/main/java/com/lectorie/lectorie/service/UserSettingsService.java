package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.*;
import com.lectorie.lectorie.dto.request.ChangeUserSettingsRequest;
import com.lectorie.lectorie.exception.custom.ImageException;
import com.lectorie.lectorie.model.Enrollment;
import com.lectorie.lectorie.model.Tutor;
import com.lectorie.lectorie.model.UserSettings;
import com.lectorie.lectorie.repository.CountryRepository;
import com.lectorie.lectorie.repository.UserSettingsRepository;
import com.lectorie.lectorie.util.ImageUtil;
import com.lectorie.lectorie.util.UserUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
public class UserSettingsService {
    private final UserSettingsRepository repository;
    private final UserUtil userUtil;
    private final CountryRepository countryRepository;

    public UserSettingsService(UserSettingsRepository repository, UserUtil userUtil, CountryRepository countryRepository) {
        this.repository = repository;
        this.userUtil = userUtil;
        this.countryRepository = countryRepository;
    }

    public UserSettingsDto uploadProfilePicture(MultipartFile file, Principal currentUser) throws IOException, DataFormatException {

        var user = userUtil.extractUser(currentUser);
        UserSettings oldUserSettings = user.getUserSettings();

        UserSettings newUserSettings = oldUserSettings.changeImageData(ImageUtil.compressImage(file.getBytes()));

        return UserSettingsDto.convert(repository.save(newUserSettings));
    }

    public byte[] downloadProfilePicture(Principal currentUser) {
        var user = userUtil.extractUser(currentUser);
        UserSettings userSettings = user.getUserSettings();

        byte[] userImage = userSettings.getImageData();

        if (userImage == null) return null;

        try {
            return ImageUtil.decompressImage(userImage);
        } catch (DataFormatException | IOException e) {
            throw new ImageException("Image exception",3990);
        }
    }

    public UserSettingsDto changeUserSettings(Principal currentUser, ChangeUserSettingsRequest changeUserSettingsRequest) throws DataFormatException, IOException {
        var user = userUtil.extractUser(currentUser);
        UserSettings userSettings = user.getUserSettings();

        UserSettings newUserSettings = userSettings
                .changeFirstName(changeUserSettingsRequest.firstName() != null ? changeUserSettingsRequest.firstName() : userSettings.getFirstName())
                .changeLastName(changeUserSettingsRequest.lastName() != null ? changeUserSettingsRequest.lastName() : userSettings.getLastName())
                .changeCountry(countryRepository.findByName(changeUserSettingsRequest.countryName()).orElseThrow(() -> new BadCredentialsException("Country value is not value")))
                .changeCity(changeUserSettingsRequest.city() != null ? changeUserSettingsRequest.city() : userSettings.getCity())
                .changePhoneCode(changeUserSettingsRequest.phoneCode())
                .changePhoneNumber(changeUserSettingsRequest.phoneNumber())
                .changeTimezone(ZoneId.of(changeUserSettingsRequest.timezone()))
                .changeDateOfBirth(changeUserSettingsRequest.dateOfBirth());

        return UserSettingsDto.convert(repository.save(newUserSettings));
    }

    public UserSettingsDto getUserSettings(Principal currentUser) throws DataFormatException, IOException {
        var user = userUtil.extractUser(currentUser);
        UserSettings userSettings = user.getUserSettings();
        return UserSettingsDto.convert(userSettings);
    }


    public UserDto getUser(Principal currentUser) {
        var user = userUtil.extractUser(currentUser);
        return UserDto.convert(user);
    }

    public TutorDto getTutor(Principal currentUser) {
        var user = userUtil.extractUser(currentUser);
        Tutor tutor = user.getTutor();
        return TutorDto.convert(tutor);
    }

    public List<BookingDto> getBookings(Principal currentUser) {
        var userSettings = userUtil.extractUser(currentUser).getUserSettings();

        List<BookingDto> bookings = new ArrayList<>();
        for (Enrollment enrollment: userSettings.getEnrollments()) {
            bookings.addAll(
                    enrollment.getBookings().stream().map(booking -> BookingDto.convert(booking, userSettings.getTimezone())).toList()
            );
        }

        return bookings;
    }

    public List<EnrollmentDto> getEnrollments(Principal currentUser) {
        var userSettings = userUtil.extractUser(currentUser).getUserSettings();

        return userSettings
                .getEnrollments()
                .stream()
                .map(enrollment -> {
                    try {
                        return EnrollmentDto.convert(enrollment);
                    } catch (DataFormatException | IOException e) {
                        // Handle the exception as needed
                        // For example, you can rethrow it as a runtime exception
                        throw new RuntimeException("Error converting Enrollment to EnrollmentDto", e);
                    }
                })

                .toList();
    }
}