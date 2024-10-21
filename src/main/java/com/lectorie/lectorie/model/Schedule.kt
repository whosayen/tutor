package com.lectorie.lectorie.model

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalTime


@Entity
data class Schedule @JvmOverloads constructor(

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    val id: Long? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "monday_time_interval")
    val monday: List<TimeInterval> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "tuesday_time_interval")
    val tuesday: List<TimeInterval> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "wednesday_time_interval")
    val wednesday: List<TimeInterval> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "thursday_time_interval")
    val thursday: List<TimeInterval> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "friday_time_interval")
    val friday: List<TimeInterval> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "saturday_time_interval")
    val saturday: List<TimeInterval> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "sunday_time_interval")
    val sunday: List<TimeInterval> = mutableListOf()
) {
    fun changeMonday(monday: List<TimeInterval>) = this.copy(monday = monday)
    fun changeTuesday(tuesday: List<TimeInterval>) = this.copy(tuesday = tuesday)
    fun changeWednesday(wednesday: List<TimeInterval>) = this.copy(wednesday = wednesday)
    fun changeThursday(thursday: List<TimeInterval>) = this.copy(thursday = thursday)
    fun changeFriday(friday: List<TimeInterval>) = this.copy(friday = friday)
    fun changeSaturday(saturday: List<TimeInterval>) = this.copy(saturday = saturday)
    fun changeSunday(sunday: List<TimeInterval>) = this.copy(sunday = sunday)

    fun getAvailableDay(dayOfWeek: DayOfWeek): List<TimeInterval> {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> monday
            DayOfWeek.TUESDAY -> tuesday
            DayOfWeek.WEDNESDAY -> wednesday
            DayOfWeek.THURSDAY -> thursday
            DayOfWeek.FRIDAY -> friday
            DayOfWeek.SATURDAY -> saturday
            DayOfWeek.SUNDAY -> sunday
        }
    }
}

