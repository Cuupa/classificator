package com.cuupa.classificator.monitor.service

import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class Event(
    val kbVersion: String,
    val text: String?,
    val results: List<String>,
    val senders: List<String>,
    val metadata: List<String>,
    val start: LocalDateTime,
    val end: LocalDateTime
) {

    val processingTime = getElapsedTime().toString() + " ms"

    override fun toString(): String {
        return ReflectionToStringBuilder.reflectionToString(this)
    }

    fun getElapsedTime() = start.until(end, ChronoUnit.MILLIS)

    fun getStartTimestamp(): Long {
        return ZonedDateTime.of(start, ZoneId.systemDefault()).toInstant().epochSecond
    }

    fun getEndTimestamp(): Long {
        return ZonedDateTime.of(end, ZoneId.systemDefault()).toInstant().epochSecond
    }

    /**
     * "KB-VERSION", "RECEIVED", "PROCESSED", "TOPICS", "SENDER", "METADATA", "TEXT"
     */
    fun toCsvString(): String {
        return StringBuilder()
            .append(kbVersion)
            .append(semicolon)
            .append(start)
            .append(semicolon)
            .append(end)
            .append(semicolon)
            .append(results.joinToString(",", "", ""))
            .append(semicolon)
            .append(senders.joinToString(",", "", ""))
            .append(semicolon)
            .append(metadata.joinToString(",", "", ""))
            .append(semicolon)
            .append(text?.replace(";", " ")?.replace("\n", " ")?.replace("\r", "") ?: "")
            .append(semicolon)
            .append(newLine())
            .toString()
    }

    companion object {
        val statisticalFields = listOf("KB-VERSION", "RECEIVED", "PROCESSED", "TOPICS", "SENDER", "METADATA", "TEXT")

        fun headlines(): String {
            return statisticalFields.joinToString(semicolon, "", newLine())
        }

        private val semicolon = ";"
        private fun newLine() = System.getProperty("line.separator")
    }
}
