package com.lectorie.lectorie.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity
data class Enrollment @JvmOverloads constructor(
    @Id
    @UuidGenerator
    val id: String? = "",

    val rate: Int? = null,

    val comment: String? = null,

    @OneToMany(mappedBy = "enrollment", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val bookings: List<Booking> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    val tutor: Tutor, // DERSI VEREN

    @ManyToOne
    @JoinColumn(name = "user-settings_id")
    val userSettings: UserSettings,
    ) {
    fun changeId(id: String?) = this.copy(id = id)
    fun changeRate(rate: Int?) = this.copy(rate = rate)
    fun changeComment(comment: String?) = this.copy(comment = comment)
    fun changeBookings(bookings: List<Booking>) = this.copy(bookings = bookings)
    fun changeTutor(tutor: Tutor) = this.copy(tutor = tutor)
    fun changeUserSettings(userSettings: UserSettings) = this.copy(userSettings = userSettings)

}
