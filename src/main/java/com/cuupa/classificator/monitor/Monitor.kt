package com.cuupa.classificator.monitor

import com.cuupa.classificator.services.kb.SemanticResult
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDate
import java.time.LocalDateTime

class Monitor(private val eventStorage: EventStorage) {

    @Value("\${classificator.monitor.enabled:false}")
    private var enabled: Boolean = false

    @Value("\${classificator.monitor.logText:false}")
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

    fun getEvents(start: LocalDate?, end: LocalDate?): List<Event> {
        return eventStorage.get(start, end)
    }

    private fun getTopics(results: List<SemanticResult>) = results.map { it.topicName }

    private fun getMetadata(results: List<SemanticResult>) = results.flatMap {
        it.metaData.map { metadata ->
            metadata.name + ":" + metadata.value
        }
    }

    private fun getSenders(results: List<SemanticResult>) = results.map { it.sender }

    companion object {
        private val log = LogFactory.getLog(Monitor::class.java)
    }
}
