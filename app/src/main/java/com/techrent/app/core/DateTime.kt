package com.techrent.app.core

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateTime {
    private val fmt = DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd

    fun parseDateOrNull(text: String): LocalDate? =
        runCatching { LocalDate.parse(text.trim(), fmt) }.getOrNull()

    fun daysBetween(start: LocalDate, end: LocalDate): Long =
        ChronoUnit.DAYS.between(start, end).coerceAtLeast(0)

    fun nowMillis(): Long = System.currentTimeMillis()
}
