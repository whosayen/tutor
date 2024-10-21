package com.lectorie.lectorie.model

import com.lectorie.lectorie.enums.BookingDuration
import com.lectorie.lectorie.enums.Status
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator
import java.time.ZonedDateTime

@Entity
data class Booking @JvmOverloads constructor(

    @Id
    @UuidGenerator
    val id: String? = "",

    val status: Status = Status.WAITING,
    val time: ZonedDateTime,

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    val enrollment: Enrollment,

    val bookingDuration: BookingDuration,
    val price: Double,
) {
    fun changeStatus(status: Status) = this.copy(status = status)
    fun changeTime(time: ZonedDateTime) = this.copy(time = time)
    fun changeEnrollment(enrollment: Enrollment) = this.copy(enrollment = enrollment)
    fun changeBookingDuration(bookingDuration: BookingDuration) = this.copy(bookingDuration = bookingDuration)
    fun changePrice(price: Double) = this.copy(price = price)
}
