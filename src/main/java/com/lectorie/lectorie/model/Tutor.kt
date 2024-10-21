package com.lectorie.lectorie.model

import com.lectorie.lectorie.enums.BookingDuration
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "tutor")
data class Tutor @JvmOverloads constructor(

    @Id
    @UuidGenerator
    val id: String? = "",

    val connectedAccountId: String? = null,
    @Size(max=10)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "tutor_id")
    val languageLevels: List<LanguageLevel> = mutableListOf(),

    @ManyToOne
    val languageToTeach: Language? = null,

    val hourlyRate: Double? = null,

    val shortDescription: String? = null,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    val videoUrl: String? = null,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    val schedule: Schedule,

    val isApproved: Boolean = false,

    @OneToMany(mappedBy = "tutor", fetch = FetchType.EAGER)
    val enrollments: List<Enrollment> = mutableListOf(),

    @OneToOne(mappedBy = "tutor", fetch = FetchType.LAZY)
    val user: User? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    val allowedBookingDurations: List<BookingDuration> = mutableListOf(BookingDuration.ONE_HOUR, BookingDuration.THIRTY_MINUTES),
    ) {
    fun changeConnectedAccountId(connectedAccountId: String?) = this.copy(connectedAccountId = connectedAccountId)
    fun changeLanguageLevels(languageLevels: List<LanguageLevel>) = this.copy(languageLevels = languageLevels)
    fun changeLanguageToTeach(languageToTeach: Language?) = this.copy(languageToTeach = languageToTeach)
    fun changeHourlyRate(hourlyRate: Double?) = this.copy(hourlyRate = hourlyRate)
    fun changeShortDescription(shortDescription: String?) = this.copy(shortDescription = shortDescription)
    fun changeDescription(description: String?) = this.copy(description = description)
    fun changeVideoUrl(videoUrl: String?) = this.copy(videoUrl = videoUrl)
    fun changeSchedule(schedule: Schedule) = this.copy(schedule = schedule)
    fun changeIsApproved(isApproved: Boolean) = this.copy(isApproved = isApproved)
    fun changeEnrollments(enrollments: List<Enrollment>) = this.copy(enrollments = enrollments)
    fun changeUser(user: User?) = this.copy(user = user)
    fun changeAllowedBookingDurations(allowedBookingDurations: List<BookingDuration>) = this.copy(allowedBookingDurations = allowedBookingDurations)

}

