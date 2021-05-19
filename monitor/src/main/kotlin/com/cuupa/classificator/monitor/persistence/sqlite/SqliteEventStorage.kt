package com.cuupa.classificator.monitor.persistence.sqlite

import com.cuupa.classificator.monitor.persistence.EventStorage
import com.cuupa.classificator.monitor.service.Event
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class SqliteEventStorage(private val eventService: EventService) : EventStorage() {

    override fun write(event: Event) = eventService.save(event)

    override fun get(start: LocalDate?, end: LocalDate?) = eventService.list().filter { inBetween(it, start, end) }

    private fun inBetween(event: Event, start: LocalDate?, end: LocalDate?): Boolean {
        val startLocal = when {
            start != null -> LocalDateTime.of(start, LocalTime.of(0, 0))
            else -> LocalDateTime.of(LocalDate.MIN, LocalTime.of(0, 0))
        }

        val endLocal = when {
            end != null -> LocalDateTime.of(end, LocalTime.of(23, 59))
            else -> LocalDateTime.of(LocalDate.MAX, LocalTime.of(23, 59))
        }
        return event.start.isAfter(startLocal) && event.end.isBefore(endLocal) || event.start == startLocal || event.end == endLocal
    }
}