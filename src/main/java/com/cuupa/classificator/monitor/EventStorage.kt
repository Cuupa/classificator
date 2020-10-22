package com.cuupa.classificator.monitor

import java.time.LocalDateTime

abstract class EventStorage {

    val statisticalFields = listOf("RECEIVED", "PROCESSED", "TEXT", "TOPICS", "SENDER", "METADATA")

    abstract fun write(event: Event)

    abstract fun get(start: LocalDateTime?, end: LocalDateTime?): List<Event>
}
