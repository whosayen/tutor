package com.lectorie.lectorie.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UuidGenerator
import java.sql.Types
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

@Entity
@Table(name = "user_settings")
data class UserSettings @JvmOverloads constructor(
    @Id
    @UuidGenerator
    val id: String? = "",

    /*
        NAME
    */
    val firstName: String,
    val lastName:String,

    /*
        PROFILE PICTURE
    */
    @Lob
    @JdbcTypeCode(Types.LONGVARBINARY)
    val imageData: ByteArray? = null,
    /*
       CONTACT INFORMATION
    */
    @ManyToOne
    @JoinColumn(name = "country_id")
    val country: Country,

    val city: String? = null,

    val phoneCode: String? = null,

    val phoneNumber: String? = null,

    val timezone: ZoneId,

    val dateOfBirth: LocalDate?,

    @OneToOne(mappedBy = "userSettings", fetch = FetchType.LAZY)
    val user: User? = null,

    @OneToMany(mappedBy = "userSettings", fetch = FetchType.EAGER)
    val enrollments: List<Enrollment> = mutableListOf(),

) {
    fun changeFirstName(firstName: String) = this.copy(firstName = firstName)
    fun changeLastName(lastName: String) = this.copy(lastName = lastName)
    fun changeImageData(imageData: ByteArray?) = this.copy(imageData = imageData)
    fun changeCountry(country: Country) = this.copy(country = country)
    fun changeCity(city: String?) = this.copy(city = city)
    fun changePhoneCode(phoneCode: String?) = this.copy(phoneCode = phoneCode)
    fun changePhoneNumber(phoneNumber: String?) = this.copy(phoneNumber = phoneNumber)
    fun changeTimezone(timezone: ZoneId) = this.copy(timezone = timezone)
    fun changeDateOfBirth(dateOfBirth: LocalDate?) = this.copy(dateOfBirth = dateOfBirth)
    fun changeUser(user: User?) = this.copy(user = user)
    fun changeEnrollments(enrollments: List<Enrollment>) = this.copy(enrollments = enrollments)

    fun getAge(): Int? {
        if (dateOfBirth == null) {
            return null
        }

        val currentDate = LocalDate.now()
        val period = Period.between(dateOfBirth, currentDate)

        return period.years
    }

}