package com.lectorie.lectorie;

import com.lectorie.lectorie.dto.AvailableIntervalDto;
import com.lectorie.lectorie.dto.LanguageDto;
import com.lectorie.lectorie.dto.LanguageLevelDto;
import com.lectorie.lectorie.dto.request.ChatMessageRequest;
import com.lectorie.lectorie.dto.request.ScheduleSettingsRequest;
import com.lectorie.lectorie.enums.Status;
import com.lectorie.lectorie.model.*;
import com.lectorie.lectorie.enums.BookingDuration;
import com.lectorie.lectorie.enums.Level;
import com.lectorie.lectorie.enums.Role;
import com.lectorie.lectorie.repository.*;
import com.lectorie.lectorie.service.*;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {

    @Value("${runner.enabled}")
    boolean runnerEnabled;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSettingsRepository userSettingsRepository;
    private final CountryRepository countryRepository;
    private final TutorRepository tutorRepository;
    private final LanguageService languageService;
    private final LanguageLevelService languageLevelService;
    private final ScheduleService scheduleService;
    private final EnrollmentRepository enrollmentRepository;
    private final ChatMessageService chatMessageService;
    private final BookingRepository bookingRepository;
    private final LanguageRepository languageRepository;

    public Runner(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSettingsRepository userSettingsRepository, CountryRepository countryRepository, TutorRepository tutorRepository, LanguageService languageService, LanguageLevelService languageLevelService, ScheduleService scheduleService, EnrollmentRepository enrollmentRepository, ChatMessageService chatMessageService, BookingRepository bookingRepository, LanguageRepository languageRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSettingsRepository = userSettingsRepository;
        this.countryRepository = countryRepository;
        this.tutorRepository = tutorRepository;
        this.languageService = languageService;
        this.languageLevelService = languageLevelService;
        this.scheduleService = scheduleService;
        this.enrollmentRepository = enrollmentRepository;
        this.chatMessageService = chatMessageService;
        this.bookingRepository = bookingRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (languageService.getAllLanguages().isEmpty()) {
            languageService.fetchAndSaveLanguages();
        }

        if (!runnerEnabled)
            return;


        User student1 = createUser("student@gmail.com","SecurePass123",Role.STUDENT,"asdf","asdf",
                1,"Turkey",null,false,true, 0);

        User student2= createUser("student2@gmail.com","SecurePass123",Role.STUDENT,"asdf","asfd",
                1,"Turkey",null,false,true, 0);

        User student3 = createUser("student3@gmail.com","SecurePass123",Role.STUDENT,"asdf","asdf",
                1,"Turkey",null,false,true, 100000);

        User admin1 = createUser("admin@gmail.com","SecurePass123",Role.ADMIN,"asdf","asdf",
                1,"Asia/Tbilisi",null,false,true, 1000000);


        Tutor tutor1 = createTutor("tutor@gmail.com","SecurePass123",Role.TUTOR,"Nurullah","Baser",
                1,"Japan",null,true,"English","Turkish", Level.C1,
                45.3,"This is a short des.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sit amet tincidunt nunc. Fusce tempor pretium sagittis. Nulla ac auctor nunc, id tincidunt libero. Fusce quis orci dignissim, aliquet mauris eu, porta est. Etiam pretium velit ut tellus tempus, molestie ornare turpis mattis. Proin tempus ullamcorper erat, at sodales neque placerat ut. Curabitur nec efficitur erat. Nulla condimentum rutrum augue at aliquam. Proin nec leo sit amet tellus egestas volutpat vitae ut sem. Proin elit nibh, hendrerit vitae molestie sed, dapibus ac leo. Proin fermentum scelerisque aliquet. Sed fermentum enim a efficitur volutpat. Quisque faucibus elit id fringilla consectetur. Donec congue consequat lorem. Ut blandit arcu vel consequat lobortis.",
                "https://www.youtube.com/watch?v=NO-EbXMB4gc",
                LocalTime.of(9, 0), LocalTime.of(19, 0), // Monday
                LocalTime.of(19, 0), LocalTime.of(21, 0), // Monday
                LocalTime.of(1, 0), LocalTime.of(5, 0), // Tuesday
                LocalTime.of(6, 0), LocalTime.of(17, 0), // Tuesday
                LocalTime.of(8, 0), LocalTime.of(10, 0), // Wednesday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Wednesday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Thursday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Thursday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Friday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Friday
                LocalTime.of(7, 0), LocalTime.of(8, 0), // Saturday
                LocalTime.of(10, 0), LocalTime.of(11, 0), // Saturday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Sunday
                LocalTime.of(15, 0), LocalTime.of(17, 0)  // Sunday
                );

        Tutor tutor2 = createTutor("tutor2@gmail.com","SecurePass123",Role.TUTOR,"asdf","asdfasdf",
                1,"Japan",null,true,"English","Turkish", Level.NATIVE,
                131.9,"This is a short des.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sit amet tincidunt nunc. Fusce tempor pretium sagittis. Nulla ac auctor nunc, id tincidunt libero. Fusce quis orci dignissim, aliquet mauris eu, porta est. Etiam pretium velit ut tellus tempus, molestie ornare turpis mattis. Proin tempus ullamcorper erat, at sodales neque placerat ut. Curabitur nec efficitur erat. Nulla condimentum rutrum augue at aliquam. Proin nec leo sit amet tellus egestas volutpat vitae ut sem. Proin elit nibh, hendrerit vitae molestie sed, dapibus ac leo. Proin fermentum scelerisque aliquet. Sed fermentum enim a efficitur volutpat. Quisque faucibus elit id fringilla consectetur. Donec congue consequat lorem. Ut blandit arcu vel consequat lobortis.",
                "https://www.youtube.com/watch?v=NO-EbXMB4gc",
                LocalTime.of(8, 0), LocalTime.of(10, 0), // Monday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Monday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Tuesday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Tuesday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Wednesday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(8, 0), LocalTime.of(10, 0), // Thursday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Friday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Friday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Saturday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Saturday
                LocalTime.of(8, 0), LocalTime.of(10, 0), // Sunday
                LocalTime.of(13, 0), LocalTime.of(15, 0)
        );





        // Additional mock tutors for testing
        createTutor("tutor3@gmail.com", "SecurePass123", Role.TUTOR, "asdf", "asdfasdf",
                1, "Japan", null, true, "English", "Turkish", Level.B2,
                70.0, "Experienced tutor.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sit amet tincidunt nunc. Fusce tempor pretium sagittis. Nulla ac auctor nunc, id tincidunt libero. Fusce quis orci dignissim, aliquet mauris eu, porta est. Etiam pretium velit ut tellus tempus, molestie ornare turpis mattis. Proin tempus ullamcorper erat, at sodales neque placerat ut. Curabitur nec efficitur erat. Nulla condimentum rutrum augue at aliquam. Proin nec leo sit amet tellus egestas volutpat vitae ut sem. Proin elit nibh, hendrerit vitae molestie sed, dapibus ac leo. Proin fermentum scelerisque aliquet. Sed fermentum enim a efficitur volutpat. Quisque faucibus elit id fringilla consectetur. Donec congue consequat lorem. Ut blandit arcu vel consequat lobortis.",
                "https://www.youtube.com/watch?v=NO-EbXMB4gc",
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Monday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Monday
                LocalTime.of(11, 0), LocalTime.of(13, 0), // Tuesday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Tuesday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Wednesday
                LocalTime.of(12, 0), LocalTime.of(14, 0), // Wednesday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Thursday
                LocalTime.of(16, 0), LocalTime.of(18, 0), // Thursday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Friday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Friday
                LocalTime.of(11, 0), LocalTime.of(13, 0), // Saturday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Saturday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Sunday
                LocalTime.of(14, 0), LocalTime.of(16, 0)  // Sunday
        );

        createTutor("tutor4@gmail.com", "SecurePass123", Role.TUTOR, "asfd", "asdfasdf",
                1, "Japan", null, true, "English", "Turkish", Level.B1,
                55.0, "Native French speaker.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sit amet tincidunt nunc. Fusce tempor pretium sagittis. Nulla ac auctor nunc, id tincidunt libero. Fusce quis orci dignissim, aliquet mauris eu, porta est. Etiam pretium velit ut tellus tempus, molestie ornare turpis mattis. Proin tempus ullamcorper erat, at sodales neque placerat ut. Curabitur nec efficitur erat. Nulla condimentum rutrum augue at aliquam. Proin nec leo sit amet tellus egestas volutpat vitae ut sem. Proin elit nibh, hendrerit vitae molestie sed, dapibus ac leo. Proin fermentum scelerisque aliquet. Sed fermentum enim a efficitur volutpat. Quisque faucibus elit id fringilla consectetur. Donec congue consequat lorem. Ut blandit arcu vel consequat lobortis.",
                "https://www.youtube.com/watch?v=NO-EbXMB4gc",
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Monday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Monday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Tuesday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Tuesday
                LocalTime.of(11, 0), LocalTime.of(13, 0), // Wednesday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(12, 0), LocalTime.of(14, 0), // Thursday
                LocalTime.of(16, 0), LocalTime.of(18, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Friday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Friday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Saturday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Saturday
                LocalTime.of(11, 0), LocalTime.of(13, 0), // Sunday
                LocalTime.of(15, 0), LocalTime.of(17, 0)  // Sunday
        );

        createTutor("tutor5@gmail.com", "SecurePass123", Role.TUTOR, "asdf", "asdfasdf",
                1, "Japan", null, true, "English", "Turkish", Level.A2,
                30.0, "Specialist in Spanish.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sit amet tincidunt nunc. Fusce tempor pretium sagittis. Nulla ac auctor nunc, id tincidunt libero. Fusce quis orci dignissim, aliquet mauris eu, porta est. Etiam pretium velit ut tellus tempus, molestie ornare turpis mattis. Proin tempus ullamcorper erat, at sodales neque placerat ut. Curabitur nec efficitur erat. Nulla condimentum rutrum augue at aliquam. Proin nec leo sit amet tellus egestas volutpat vitae ut sem. Proin elit nibh, hendrerit vitae molestie sed, dapibus ac leo. Proin fermentum scelerisque aliquet. Sed fermentum enim a efficitur volutpat. Quisque faucibus elit id fringilla consectetur. Donec congue consequat lorem. Ut blandit arcu vel consequat lobortis.",
                "https://www.youtube.com/watch?v=NO-EbXMB4gc",
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Monday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Monday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Tuesday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Tuesday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Wednesday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Wednesday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Thursday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Thursday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Friday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Friday
                LocalTime.of(11, 0), LocalTime.of(13, 0), // Saturday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Saturday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Sunday
                LocalTime.of(14, 0), LocalTime.of(16, 0)  // Sunday
        );

        createTutor("tutor6@gmail.com", "SecurePass123", Role.TUTOR, "asdfasdf", "asdfasdf",
                1, "Japan", null, true, "English", "Turkish", Level.C2,
                95.0, "Fluent in Italian.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sit amet tincidunt nunc. Fusce tempor pretium sagittis. Nulla ac auctor nunc, id tincidunt libero. Fusce quis orci dignissim, aliquet mauris eu, porta est. Etiam pretium velit ut tellus tempus, molestie ornare turpis mattis. Proin tempus ullamcorper erat, at sodales neque placerat ut. Curabitur nec efficitur erat. Nulla condimentum rutrum augue at aliquam. Proin nec leo sit amet tellus egestas volutpat vitae ut sem. Proin elit nibh, hendrerit vitae molestie sed, dapibus ac leo. Proin fermentum scelerisque aliquet. Sed fermentum enim a efficitur volutpat. Quisque faucibus elit id fringilla consectetur. Donec congue consequat lorem. Ut blandit arcu vel consequat lobortis.",
                "https://www.youtube.com/watch?v=NO-EbXMB4gc",
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Monday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Monday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Tuesday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Tuesday
                LocalTime.of(11, 0), LocalTime.of(13, 0), // Wednesday
                LocalTime.of(15, 0), LocalTime.of(17, 0), // Wednesday
                LocalTime.of(12, 0), LocalTime.of(14, 0), // Thursday
                LocalTime.of(16, 0), LocalTime.of(18, 0), // Thursday
                LocalTime.of(9, 0), LocalTime.of(11, 0), // Friday
                LocalTime.of(13, 0), LocalTime.of(15, 0), // Friday
                LocalTime.of(10, 0), LocalTime.of(12, 0), // Saturday
                LocalTime.of(14, 0), LocalTime.of(16, 0), // Saturday
                LocalTime.of(11, 0), LocalTime.of(13, 0), // Sunday
                LocalTime.of(15, 0), LocalTime.of(17, 0)  // Sunday
        );

        // student1 is Tahir Akdeniz
        // tutor1 is Nurullah Baser
        // tutor2 is Zeynep Yesilkaya

        User tutorUser = userRepository.findByEmail("tutor@gmail.com").orElseThrow( () -> new RuntimeException("Tutor not found"));
        User tutor2User = userRepository.findByEmail("tutor2@gmail.com").orElseThrow( () -> new RuntimeException("Tutor not found"));
        

        ChatMessageRequest messageRequest1 = new ChatMessageRequest(
                tutorUser.getId(),
                "Hello, I would like to schedule a session with you!"
        );

        ChatMessageRequest messageRequest2 = new ChatMessageRequest(
                student1.getId(),
                "Hello, Thanks for reaching out! I am available on Monday and Wednesday from 10:00 to 12:00."
        );

        ChatMessageRequest messageRequest3 = new ChatMessageRequest(
                tutorUser.getId(),
                "Great! I am available on Monday and Wednesday from 10:00 to 12:00. Let's schedule a session for Monday at 10:00."
        );

        ChatMessageRequest messageRequest4 = new ChatMessageRequest(
                student1.getId(),
                "Sounds good! I will book the session for Monday at 10:00."
        );

        ChatMessageRequest messageRequest5 = new ChatMessageRequest(
                tutorUser.getId(),
                "Hello, Friend How are you?"
        );

        ChatMessageRequest messageRequest6 = new ChatMessageRequest(
                tutor2User.getId(),
                "Hello, I am fine. How are you?"
        );

        chatMessageService.save(messageRequest1, student1);
        chatMessageService.save(messageRequest2, tutorUser);
        chatMessageService.save(messageRequest3, student1);
        chatMessageService.save(messageRequest4, tutorUser);
        chatMessageService.save(messageRequest5, tutor2User);
        chatMessageService.save(messageRequest6, tutorUser);


        createBooking(tutor1, student1, ZonedDateTime.now().minusMinutes(120));
        createBooking(tutor1, student1, ZonedDateTime.now().plusMinutes(120));
        createBooking(tutor1, student1, ZonedDateTime.now().plusDays(2).minusMinutes(240));
        createBooking(tutor1, student1, ZonedDateTime.now().plusDays(3));
        createBooking(tutor1, student3, ZonedDateTime.now().plusDays(3).plusMinutes(30));


    }


    private ZonedDateTime roundToNearestHalfHour(ZonedDateTime dateTime) {
        int minutes = dateTime.getMinute();

        if (minutes < 15) {
            return dateTime.withMinute(0).withSecond(0).withNano(0);
        } else if (minutes < 45) {
            return dateTime.withMinute(30).withSecond(0).withNano(0);
        } else {
            return dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        }
    }



    private void createBooking(Tutor tutor, User student, ZonedDateTime zonedDateTime) {
        zonedDateTime = roundToNearestHalfHour(zonedDateTime);

        Enrollment enrollment1 =
                enrollmentRepository.findEnrollmentsByUserSettingsAndTutor(student.getUserSettings(), tutor)
                        .orElseGet(() ->
                                enrollmentRepository.save(
                                        new Enrollment(
                                                tutorRepository.findById(tutor.getId())
                                                        .orElseThrow(),
                                                userSettingsRepository.findById(student.getUserSettings().getId())
                                                        .orElseThrow()
                                        )
                                )
                        );

        Booking booking1 = bookingRepository.save(
                new Booking(
                        "",
                        (ZonedDateTime.now().isBefore(zonedDateTime)) ? Status.WAITING : Status.DONE,
                        zonedDateTime,
                        enrollment1,
                        BookingDuration.THIRTY_MINUTES,
                        5000.0
                )
        );

        enrollmentRepository.save(enrollment1);

    }

    private User createUser(String email, String password, Role role, String firstName, String lastName, int country,
                            String zone, LocalDate dateOfBirth, boolean isTutor, boolean isEnabled, double balance) {

        UserSettings userSettings = new UserSettings(firstName,lastName,countryRepository.findById(country).orElseGet(null),ZoneId.of(zone),dateOfBirth);

        return userRepository.save(
                new User(
                        email,
                        passwordEncoder.encode(password),
                        role,
                        userSettingsRepository.save(userSettings),
                        isTutor ? tutorRepository.save(new Tutor(scheduleService.createSchedule())) : null,
                        isEnabled,
                        balance,
                        null
                )
        );
    }


    private Tutor createTutor(String email, String password, Role role, String firstName, String lastName, int country,
                             String zone, LocalDate dateOfBirth, boolean isEnabled, String subjectTaught,
                             String spokenLanguage, Level level, Double hourlyRate, String shortDescription, String description,
                             String videoUrl,
                             LocalTime mondayStart1, LocalTime mondayEnd1,
                             LocalTime mondayStart2, LocalTime mondayEnd2,
                             LocalTime tuesdayStart1, LocalTime tuesdayEnd1,
                             LocalTime tuesdayStart2, LocalTime tuesdayEnd2,
                             LocalTime wednesdayStart1, LocalTime wednesdayEnd1,
                             LocalTime wednesdayStart2, LocalTime wednesdayEnd2,
                             LocalTime thursdayStart1, LocalTime thursdayEnd1,
                             LocalTime thursdayStart2, LocalTime thursdayEnd2,
                             LocalTime fridayStart1, LocalTime fridayEnd1,
                             LocalTime fridayStart2, LocalTime fridayEnd2,
                             LocalTime saturdayStart1, LocalTime saturdayEnd1,
                             LocalTime saturdayStart2, LocalTime saturdayEnd2,
                             LocalTime sundayStart1, LocalTime sundayEnd1,
                             LocalTime sundayStart2, LocalTime sundayEnd2
                             ) {


        List<AvailableIntervalDto> mondayAvailableTimes = List.of(
                new AvailableIntervalDto(mondayStart1, mondayEnd1),
                new AvailableIntervalDto(mondayStart2, mondayEnd2)
        );

        List<AvailableIntervalDto> tuesdayAvailableTimes = List.of(
                new AvailableIntervalDto(tuesdayStart1, tuesdayEnd1),
                new AvailableIntervalDto(tuesdayStart2, tuesdayEnd2)
        );

        List<AvailableIntervalDto> wednesdayAvailableTimes = List.of(
                new AvailableIntervalDto(wednesdayStart1, wednesdayEnd1),
                new AvailableIntervalDto(wednesdayStart2, wednesdayEnd2)
        );

        List<AvailableIntervalDto> thursdayAvailableTimes = List.of(
                new AvailableIntervalDto(thursdayStart1, thursdayEnd1),
                new AvailableIntervalDto(thursdayStart2, thursdayEnd2)
        );
        List<AvailableIntervalDto> fridayAvailableTimes = List.of(
                new AvailableIntervalDto(fridayStart1, fridayEnd1),
                new AvailableIntervalDto(fridayStart2, fridayEnd2)
        );

        List<AvailableIntervalDto> saturdayAvailableTimes = List.of(
                new AvailableIntervalDto(saturdayStart1, saturdayEnd1),
                new AvailableIntervalDto(saturdayStart2, saturdayEnd2)
        );

        List<AvailableIntervalDto> sundayAvailableTimes = List.of(
                new AvailableIntervalDto(sundayStart1, sundayEnd1),
                new AvailableIntervalDto(sundayStart2, sundayEnd2)
        );

        ScheduleSettingsRequest scheduleSettingsRequest = new ScheduleSettingsRequest(
                mondayAvailableTimes,
                tuesdayAvailableTimes,
                wednesdayAvailableTimes,
                thursdayAvailableTimes,
                fridayAvailableTimes,
                saturdayAvailableTimes,
                sundayAvailableTimes
        );

        User user = createUser(email,password,role,firstName,lastName,country,zone,dateOfBirth,true,isEnabled, 0);
        Tutor tutor = user.getTutor();

        Language lang = languageRepository.findById(spokenLanguage).orElseThrow();
        LanguageDto languageDto = new LanguageDto(lang.getName(), lang.getCode());
        LanguageLevelDto languageLevelDto = new LanguageLevelDto(languageDto,level);
        List<LanguageLevelDto> languageLevelDtos = new ArrayList<>();
        languageLevelDtos.add(languageLevelDto);

        scheduleService.setTutorScheduleWithIntervals(tutor.getSchedule().getId(), scheduleSettingsRequest);

        return tutorRepository.save(new Tutor(
                        tutor.getId(),
                        tutor.getConnectedAccountId(),
                        languageLevelService.saveAllLanguages(languageLevelDtos),
                        languageService.findById(subjectTaught),
                        hourlyRate,
                        shortDescription,
                        description,
                        videoUrl,
                        tutor.getSchedule(),
                        true,
                        tutor.getEnrollments(),
                        tutor.getUser(),
                        tutor.getAllowedBookingDurations()
                )
        );
    }

}
