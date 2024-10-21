package com.lectorie.lectorie.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_verification_token")
data class EmailVerificationToken @JvmOverloads constructor(


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val email: String,
    val otp: String,
    val expirationTime: LocalDateTime,
    val isEnabled: Boolean = false
) {
    fun changeEmail(email: String) = this.copy(email = email)
    fun changeOtp(otp: String) = this.copy(otp = otp)
    fun changeExpirationTime(expirationTime: LocalDateTime) = this.copy(expirationTime = expirationTime)
    fun changeIsEnabled(isEnabled: Boolean) = this.copy(isEnabled = isEnabled)

}
