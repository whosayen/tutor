package com.lectorie.lectorie.enums

enum class BookingDuration(val durationMinutes: Int) {
    THIRTY_MINUTES(30),
    FORTY_FIVE_MINUTES(45),
    ONE_HOUR(60),
    ONE_AND_HALF_HOUR(90);

    companion object {
        fun fromMinutes(minutes: Int): BookingDuration? {
            return entries.find { it.durationMinutes == minutes }
        }
    }

}


