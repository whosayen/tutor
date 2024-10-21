package com.lectorie.lectorie.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "language")
data class Language(

    @Id
    val name: String,
    val code: String
) {
    fun changeName(name: String) = this.copy(name = name)
}
