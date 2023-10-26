package com.fas.dev.latihan.advancedtesting.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateFormatter {
    fun formatDate(dateString: String, targetTimeZone: String): String {
        val instant = Instant.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }
}