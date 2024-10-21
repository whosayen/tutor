package com.lectorie.lectorie.service;

import com.lectorie.lectorie.dto.FullUserDto;
import com.lectorie.lectorie.dto.*;
import com.lectorie.lectorie.dto.request.*;
import com.lectorie.lectorie.exception.custom.BookingException;
import com.lectorie.lectorie.exception.custom.BookingNotFoundException;
import com.lectorie.lectorie.exception.custom.TutorNotFoundException;
import com.lectorie.lectorie.model.*;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.enums.Level;
import com.lectorie.lectorie.enums.Status;
import com.lectorie.lectorie.repository.*;
import com.lectorie.lectorie.specification.TutorSpecifications;
import com.lectorie.lectorie.util.UserUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
public class TutorService {
    private final TutorRepository repository;
    private final UserUtil userUtil;
    private final LanguageService languageService;
    private final LanguageLevelService languageLevelService;
    private final ScheduleService scheduleService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;


    public TutorService(TutorRepository repository, UserUtil userUtil, LanguageService languageService, LanguageLevelService languageLevelService, ScheduleService scheduleService, BookingRepository bookingRepository, UserRepository userRepository, LanguageRepository languageRepository) {
        this.repository = repository;
        this.userUtil = userUtil;
        this.languageService = languageService;
        this.languageLevelService = languageLevelService;
        this.scheduleService = scheduleService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
    }

    public TutorDto changeTutorSubjectTaught(LanguageDto languageSpokenRequest, Principal currentUser) {
        var tutor = userUtil.getTutorFromPrincipal(currentUser);

        Tutor newTutor = tutor.changeLanguageToTeach(languageService.findById(languageSpokenRequest.languageName()));

        return TutorDto.convert(repository.save(newTutor));
    }

    public TutorDto changeTutorSpokenLanguages(ChangeTutorSpokenLanguagesRequest changeTutorSpokenLanguagesRequest, Principal currentUser) {
        var tutor = userUtil.getTutorFromPrincipal(currentUser);

        List<Long> deleteLanguageLevels = tutor.getLanguageLevels().stream().map(LanguageLevel::getId).toList();
        languageLevelService.deleteAllById(deleteLanguageLevels);

        Tutor newTutor = tutor.changeLanguageLevels(languageLevelService.saveAllLanguages(changeTutorSpokenLanguagesRequest.languageLevelDtos()));

        return TutorDto.convert(repository.save(newTutor));
    }

    public TutorDto changeHourlyRate(HourlyRateRequest hourlyRateRequest, Principal currentUser) {
        var tutor = userUtil.getTutorFromPrincipal(currentUser);

        Tutor newTutor = tutor.changeHourlyRate(hourlyRateRequest.hourlyRate());

        return TutorDto.convert(repository.save(newTutor));
    }

    public TutorDto changeTutorDescriptionSettings(DescriptionSettingsRequest descriptionSettingsRequest, Principal currentUser) {
        var tutor = userUtil.getTutorFromPrincipal(currentUser);

        Tutor newTutor = tutor.changeShortDescription(descriptionSettingsRequest.shortDescription()).changeDescription(descriptionSettingsRequest.description());

        return TutorDto.convert(repository.save(newTutor));
    }

    public TutorDto changeVideoSettings(VideoSettingsRequest videoSettingsRequest, Principal currentUser) {
        var tutor = userUtil.getTutorFromPrincipal(currentUser);


        Tutor newTutor = tutor.changeVideoUrl(videoSettingsRequest.videoUrl());

        return TutorDto.convert(repository.save(newTutor));
    }

    public TutorDto changeScheduleSettings(ScheduleSettingsRequest scheduleSettingsRequest, Principal currentUser) {
        var tutor = userUtil.getTutorFromPrincipal(currentUser);

        var tutorSchedule =  tutor.getSchedule().getId();
        scheduleService.setTutorScheduleWithIntervals(tutorSchedule, scheduleSettingsRequest);

        return TutorDto.convert(
                repository.save(
                        tutor
                )
        );
    }


    protected Tutor getTutor(String tutorId) {
        return repository.findById(tutorId)
                .orElseThrow(() -> new TutorNotFoundException("no such tutor exception",3990));
    }

    protected Tutor createTutor() {
        var tutor = new Tutor(scheduleService.createSchedule()); //TODO SUS GELDI BU
        return repository.save(tutor);
    }

    protected TutorDto approve(User user, Boolean isApproved) {
        if (!isApproved) {
            userRepository.save(user.changeIsAccountNonLocked(false));
        }

        var tutor = user.getTutor();
        Tutor newTutor = tutor.changeIsApproved(isApproved);

        return TutorDto.convert(repository.save(newTutor));
    }

    public Boolean checkApproved(Principal currentUser) {
        var tutor = userUtil.getTutorFromPrincipal(currentUser);
        return tutor.isApproved();
    }

    protected List<BookingDuration> getBookingDurations(String tutorId) {
        return repository.findById(tutorId).orElseThrow(
                () -> new TutorNotFoundException("tutor not found", 1234))
                .getAllowedBookingDurations();
    }

    public List<BookingDto> getBookings(Principal currentUser, ZonedDateTime startTime, ZonedDateTime finishTime, Status status) {
        Tutor tutor = userUtil.getTutorFromPrincipal(currentUser);

        List<BookingDto> bookings = new ArrayList<>();
        for (Enrollment enrollment : tutor.getEnrollments()) {
            bookings.addAll(
                    enrollment.getBookings().stream()
                            .filter(booking -> isBookingInRange(booking, startTime, finishTime) &&
                                    (status == null || booking.getStatus() == status))
                            .map(booking -> BookingDto.convert(booking, tutor.getUser().getUserSettings().getTimezone()))
                            .toList()
            );
        }

        return bookings;
    }

    public BookingDto answerBookingRequest(Principal currentUser, String bookingId, boolean accepted) {
        UserSettings tutorUs = userUtil.extractUser(currentUser).getUserSettings();

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking not found.",4044));

        if (booking.getStatus() != Status.WAITING) {
           throw new BookingException("booking is not waiting.",3990);
        }

        Booking newBooking = booking.changeStatus(accepted ? Status.CONFIRMED : Status.DECLINED);

        User user = booking.getEnrollment().getUserSettings().getUser();

        if (!accepted) {
            userRepository.save(
                    user.changeBalance(user.getBalance() + booking.getPrice())
            );
        }
        System.out.println("asdfasdf");

        return BookingDto.convert(bookingRepository.save(newBooking), tutorUs.getTimezone());
    }

    private boolean isBookingInRange(Booking booking, ZonedDateTime startTime, ZonedDateTime finishTime) {
        return !booking.getTime().isBefore(startTime) && !booking.getTime().isAfter(finishTime);
    }

    private Page<Tutor> searchTutors(Level languageLevel, Language knownLanguage, Language languageToTeach, Double minRate, Double maxRate, String keyword, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Specification<Tutor> spec = Specification.where(null);

        if (languageLevel != null && knownLanguage != null) {
            spec = spec.and(TutorSpecifications.hasLanguageLevel(languageLevel, knownLanguage));
        }
        if (languageToTeach != null) {
            spec = spec.and(TutorSpecifications.hasLanguageToTeach(languageToTeach));
        }
        if (minRate != null || maxRate != null) {
            spec = spec.and(TutorSpecifications.hasHourlyRateBetween(minRate, maxRate));
        }
        if (StringUtils.hasText(keyword)) {
            spec = spec.and(TutorSpecifications.hasShortDescriptionOrFirstNameOrLastNameContaining(keyword));
        }

        return repository.findAll(spec, pageable);
    }


    public Page<FullUserSearchDto> searchTutor(Level languageLevel,
                                               String knownLanguage,
                                               String languageToTeach,
                                               Double minRate,
                                               Double maxRate,
                                               String keyword,
                                               int page,
                                               int size,
                                               String sortBy) {

        Language knownLanguageEntity = null;
        if (languageLevel != null) {
            knownLanguageEntity = languageRepository.findById(knownLanguage)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown language in known language: " + knownLanguage));
        }

        Language languageToTeachEntity = null;
        if (languageToTeach != null) {
            languageToTeachEntity = languageRepository.findById(languageToTeach)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown language in language to teach: " + knownLanguage));
        }

        Page<Tutor> pageResult = searchTutors(languageLevel, knownLanguageEntity, languageToTeachEntity, minRate, maxRate, keyword, page, size, sortBy);
        Page<FullUserSearchDto> dtoPage = pageResult.map(from -> {
            try {
                return FullUserSearchDto.convert(from);
            } catch (DataFormatException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        return dtoPage;
    }
}
