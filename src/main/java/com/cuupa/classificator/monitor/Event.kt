package com.cuupa.classificator.monitor

import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import java.time.LocalDateTime

data class Event(val text: String?, val results: List<String>, val metadata: List<String>, val senders: List<String>,
                 val start: LocalDateTime, val end: LocalDateTime) {

    constructor(results: List<String>, metadata: List<String>, senders: List<String>, start: LocalDateTime,
                end: LocalDateTime) : this(null, results, metadata, senders, start, end)

    override fun toString(): String {
        return ReflectionToStringBuilder.reflectionToString(this)
    }

}
