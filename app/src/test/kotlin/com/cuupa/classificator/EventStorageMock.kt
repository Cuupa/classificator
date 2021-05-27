package com.cuupa.classificator

import com.cuupa.classificator.monitor.persistence.EventStorage
import com.cuupa.classificator.monitor.service.Event
import java.time.LocalDate

/**
 * @author Simon Thiel (https://github.com/cuupa) (27.05.2021)
 */
class EventStorageMock : EventStorage() {
    override fun write(event: Event) = Unit

    override fun get(start: LocalDate?, end: LocalDate?) = listOf<Event>()
}