package com.cuupa.classificator.monitor.sqlite

import com.cuupa.classificator.monitor.Event

class EventService(private val eventRepository: EventRepository) {

    fun list(): List<Event> {
        return eventRepository.findAll().map { mapToDomainObject(it) }
    }

    fun save(event: Event) {
        eventRepository.save(mapToEntity(event))
    }

    private fun mapToEntity(event: Event): EventEntity {
        val entity = EventEntity()
        entity.kbVersion = event.kbVersion
        entity.start = event.start
        entity.end = event.end
        entity.metadata = event.metadata.joinToString(";", "", "")
        entity.results = event.results.joinToString(";", "", "")
        entity.senders = event.senders.joinToString(";", "", "")
        entity.text = event.text
        return entity
    }

    private fun mapToDomainObject(it: EventEntity): Event {
        return Event(
            it.kbVersion,
            it.text,
            it.results.split(";"),
            it.senders.split(";"),
            it.metadata.split(";"),
            it.start,
            it.end
        )
    }
}