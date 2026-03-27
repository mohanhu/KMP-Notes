package org.codeloop.notes.utils.zonetimer

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

object ZoneTimer {

    fun formatDate(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.day}/${localDateTime.month.number}/${localDateTime.year}"
    }

    fun formatTime(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.hour}:${localDateTime.minute} ${localDateTime.hour.timeMode()}"
    }

    fun toMs(timeStamp: String) : Long {
        return Instant.parse(timeStamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }

    private fun Int.timeMode() : String {

        return if (this < 12) "AM"
        else "PM"

    }
}