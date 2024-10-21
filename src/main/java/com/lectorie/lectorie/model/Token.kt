package com.lectorie.lectorie.model

import com.lectorie.lectorie.enums.TokenType
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "tokens")
data class Token @JvmOverloads constructor (

    @Id
    @UuidGenerator
    val id: String? = "",
    val token: String,
    @Enumerated(EnumType.STRING)
    val tokenType: TokenType,
    var expired: Boolean,
    var revoked: Boolean,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User

) {
    fun changeToken(token: String) = this.copy(token = token)
    fun changeTokenType(tokenType: TokenType) = this.copy(tokenType = tokenType)
    fun changeExpired(expired: Boolean) = this.copy(expired = expired)
    fun changeRevoked(revoked: Boolean) = this.copy(revoked = revoked)
    fun changeUser(user: User) = this.copy(user = user)
}


