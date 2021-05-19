package com.cuupa.classificator.monitor.persistence.sqlite

import com.cuupa.classificator.monitor.service.Event

class EventService(private val eventRepository: EventRepository) {

    fun list(): List<Event> {
        return try {
            eventRepository.findAll().map { mapToDomainObject(it) }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun save(event: Event) {
        eventRepository.save(mapToEntity(event))
    }

    private fun mapToEntity(event: Event): EventEntity {
        return EventEntity().apply {
            kbVersion = event.kbVersion
            start = event.start
            end = event.end
            metadata = event.metadata.joinToString(";", "", "")
            results = event.results.joinToString(";", "", "")
            senders = event.senders.joinToString(";", "", "")
            text = event.text
        }
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