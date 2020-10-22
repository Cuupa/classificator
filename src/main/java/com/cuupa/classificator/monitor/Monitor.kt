package com.cuupa.classificator.monitor

import com.cuupa.classificator.services.kb.SemanticResult
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDateTime

class Monitor(private val eventStorage: EventStorage) {

    @Value("\${classificator.monitor.enabled}")
    private var enabled: Boolean = false

    @Value("\${classificator.monitor.logText}")
    private var logText: Boolean = false

    fun writeEvent(text: String?, results: List<SemanticResult>, start: LocalDateTime, end: LocalDateTime) {
        if (enabled) {
            val event = if (logText) {
                Event(text, getTopics(results), getMetadata(results), getSenders(results), start, end)
            } else {
                Event(getTopics(results), getMetadata(results), getSenders(results), start, end)
            }
            eventStorage.write(event)
            if (log.isDebugEnabled) {
                log.debug("Event $event monitored")
            }
        }
    }

    fun getEvents(start: LocalDateTime?, end: LocalDateTime?): List<Event> {
        return eventStorage.get(start, end)
    }

    fun getTopics(results: List<SemanticResult>) = results.map { it.topicName }

    fun getMetadata(results: List<SemanticResult>) = results.flatMap {
        it.metaData.map { metadata ->
            metadata.name + ":" + metadata.value
        }
    }

    fun getSenders(results: List<SemanticResult>) = results.map { it.sender }

    companion object {
        private val log = LogFactory.getLog(Monitor::class.java)
    }
}
