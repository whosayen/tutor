package com.lectorie.lectorie.model

import jakarta.persistence.Embeddable
import java.time.LocalTime

@Embeddable
data class TimeInterval( // TODO start ve end startTime ve endTime a cevrilebilir
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    fun changeStartTime(startTime: LocalTime) = this.copy(startTime = startTime)
    fun changeEndTime(endTime: LocalTime) = this.copy(endTime = endTime)
}
