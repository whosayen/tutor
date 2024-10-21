package com.lectorie.lectorie.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "country")
data class Country @JvmOverloads constructor(   //TODO CHANGE ID TO NAME
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    val name: String,
    val code: String
) {
    fun changeName(name: String) = this.copy(name = name)
    fun changeCode(code: String) = this.copy(code = code)
}
