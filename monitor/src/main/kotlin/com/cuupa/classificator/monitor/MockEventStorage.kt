package com.cuupa.classificator.monitor

import java.time.LocalDate

class MockEventStorage : EventStorage() {

    val storage = mutableListOf<Event>()

    override fun write(event: Event) {
        storage.add(event)
    }

    override fun get(start: LocalDate?, end: LocalDate?): List<Event> {
        return listOf()
    }
}
