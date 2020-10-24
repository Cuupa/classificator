package com.cuupa.classificator.monitor

import java.time.LocalDate

abstract class EventStorage {

    val statisticalFields = listOf("RECEIVED", "PROCESSED", "TEXT", "TOPICS", "SENDER", "METADATA")

    abstract fun write(event: Event)

    abstract fun get(start: LocalDate?, end: LocalDate?): List<Event>
}
