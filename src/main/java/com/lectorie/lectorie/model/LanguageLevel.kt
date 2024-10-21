package com.lectorie.lectorie.model

import com.lectorie.lectorie.enums.Level
import jakarta.persistence.*

@Entity
@Table
data class LanguageLevel @JvmOverloads constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne // dursun bi
    val language: Language,

    @Enumerated(EnumType.STRING)
    val level: Level

) {
    fun changeId(id: Long?) = this.copy(id = id)
    fun changeLanguage(language: Language) = this.copy(language = language)
    fun changeLevel(level: Level) = this.copy(level = level)

}
