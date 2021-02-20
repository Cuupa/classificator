package com.cuupa.classificator.monitor

import com.cuupa.classificator.services.kb.SemanticResult
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToLong

class Monitor(private val eventStorage: EventStorage) {

    @Value("\${classificator.monitor.enabled:false}")
    private var enabled: Boolean = false

    @Value("\${classificator.monitor.logText:false}")
    private var logText: Boolean = false

    fun writeEvent(text: String?, results: List<SemanticResult>, start: LocalDateTime, end: LocalDateTime) {
        if (enabled) {
            val event = if (logText) {
                Event(text, getTopics(results), getSenders(results), getMetadata(results), start, end)
            } else {
                Event(getTopics(results), getSenders(results), getMetadata(results), start, end)
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

    fun getStatistics(start: LocalDate?, end: LocalDate?): MonitorStatistics {
        val events = getEvents(start, end)
        val monitorStatistics = MonitorStatistics()
        monitorStatistics.topicDistribution = getTopicDistribution(events)
        monitorStatistics.senderDistribution = getSenderDistribution(events)
        monitorStatistics.maxProcessingTime = events.maxOf { it.getElapsedTime() }
        monitorStatistics.minProcessingTime = events.minOf { it.getElapsedTime() }
        val elapsedTime = events.map { it.getElapsedTime() }
            .average()

        monitorStatistics.averageProcessingTime = if (elapsedTime.isNaN()) {
            0L
        } else {
            elapsedTime.toLong()
        }

        val average = events.filter { !it.text.isNullOrBlank() }
            .map { it.text!!.length }
            .average()
        monitorStatistics.averageTextLength = if (!average.isNaN()) {
            average.roundToLong()
        } else {
            0L
        }

        return monitorStatistics
    }

    private fun getTopicDistribution(events: List<Event>): Map<String, Int> {
        val resultMap = mutableMapOf<String, Int>()
        events.forEach(calculateDistributionForResults(resultMap))
        return resultMap
    }

    private fun getSenderDistribution(events: List<Event>): MutableMap<String, Int> {
        val resultMap = mutableMapOf<String, Int>()
        events.forEach(calculateDistributionsForSenders(resultMap))
        return resultMap
    }

    private fun calculateDistributionForEntry(resultMap: MutableMap<String, Int>, it: String) {
        if (resultMap.containsKey(it)) {
            resultMap[it] = resultMap[it]!! + 1
        } else {
            resultMap[it] = 1
        }
    }

    private fun calculateDistributionForResults(resultMap: MutableMap<String, Int>): (Event) -> Unit {
        return { event ->
            event.results.forEach {
                calculateDistributionForEntry(resultMap, it)
            }
        }
    }

    private fun calculateDistributionsForSenders(resultMap: MutableMap<String, Int>): (Event) -> Unit {
        return { event ->
            event.senders.forEach {
                calculateDistributionForEntry(resultMap, it)
            }
        }
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
