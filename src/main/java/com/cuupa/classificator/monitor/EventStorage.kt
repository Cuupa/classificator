package com.cuupa.classificator.monitor

import java.time.LocalDate

abstract class EventStorage {

    abstract fun write(event: Event)

    abstract fun get(start: LocalDate?, end: LocalDate?): List<Event>
}
