package com.lectorie.lectorie.enums

enum class Status {
    CONFIRMED, // WAITING -> CONFIRMED
    WAITING, // --
    DECLINED, // WAITING -> DECLINE
    RESCHEDULED, // CONFIRMED, WAITING -> RESCHEDULED ////
    DONE,
    NOT_DONE
}