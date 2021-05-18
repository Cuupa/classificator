package com.cuupa.classificator.monitor

import java.time.LocalDate

class MockEventStorage : EventStorage() {
    override fun write(event: Event) {

    }

    override fun get(start: LocalDate?, end: LocalDate?): List<Event> {
        return listOf()
    }
}
