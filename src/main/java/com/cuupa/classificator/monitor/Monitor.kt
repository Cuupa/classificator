package com.cuupa.classificator.monitor

import com.cuupa.classificator.knowledgebase.resultobjects.SemanticResult
import org.apache.commons.lang3.time.DateUtils
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.stream.IntStream
import kotlin.math.roundToLong

class Monitor(private val eventStorage: EventStorage) {

    @Value("\${classificator.monitor.enabled:false}")
    private var enabled: Boolean = false

    @Value("\${classificator.monitor.logText:false}")
    private var logText: Boolean = false

    private val hours = mutableListOf<String>()

    private val format = SimpleDateFormat("HH:mm")

    private val formatDate = SimpleDateFormat("dd.MM.yyyy HH:mm")

    init {
        val calendar = GregorianCalendar.getInstance()
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0)
        hours.add(format.format(calendar.time))
        IntStream.range(0, 23).forEach {
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            hours.add(format.format(calendar.time))
        }
    }

    fun writeEvent(
        version: String,
        text: String?,
        results: List<SemanticResult>,
        start: LocalDateTime,
        end: LocalDateTime
    ) {
        if (enabled) {
            val event = if (logText) {
                Event(version, text, getTopics(results), getSenders(results), getMetadata(results), start, end)
            } else {
                Event(version, getTopics(results), getSenders(results), getMetadata(results), start, end)
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
        if(events.isNotEmpty()) {
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

            val processingHistory = groupBy(events)
                .mapValues { it.value.map { event -> event.getElapsedTime() }.average() }.mapValues { getDefault(it) }
                .mapValues { it.value / 1000 }.toList().sortedBy { it.first }.toMap().toMutableMap()

            monitorStatistics.processingHistory = fillEmptyTimeSlots(processingHistory)
        }
        return monitorStatistics
    }

    private fun fillEmptyTimeSlots(processingHistory: MutableMap<String, Double>): Map<String, Double> {

        val last = processingHistory.keys.last().split(" ".toRegex())
        var index = 0
        var insert = false
        var condition = true
        while (condition) {
            if (hours[index] == last[1] && !insert) {
                insert = true
            } else if (hours[index] != last[1] && insert) {
                processingHistory[last[0] + " " + hours[index]] = 0.0
            } else if (hours[index] == last[1] && insert) {
                condition = false
            }

            if (index + 1 == hours.size) {
                index = 0
            } else {
                index++
            }
        }
        return processingHistory
    }

    private fun getDefault(it: Map.Entry<String, Double>) = if (it.value.isNaN()) {
        0.0
    } else {
        it.value
    }


    private fun groupBy(
        events: List<Event>,
    ): Map<String, List<Event>> {
        val calendar = GregorianCalendar.getInstance()
        return events.groupBy {
            val date =
                DateUtils.round(Date.from(it.start.atZone(ZoneId.systemDefault()).toInstant()), Calendar.HOUR_OF_DAY)
            calendar.time = date
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, 0)
            formatDate.format(calendar.time)
        }
    }


    private fun getTopicDistribution(events: List<Event>): Map<String, Int> {
        val resultMap = mutableMapOf<String, Int>()
        events.forEach(calculateDistributionForResults(resultMap))
        return resultMap.toList().sortedByDescending { (_, value) -> value }.toMap()


    }

    private fun getSenderDistribution(events: List<Event>): Map<String, Int> {
        val resultMap = mutableMapOf<String, Int>()
        events.forEach(calculateDistributionsForSenders(resultMap))
        return resultMap.toList().sortedByDescending { (_, value) -> value }.toMap()
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
