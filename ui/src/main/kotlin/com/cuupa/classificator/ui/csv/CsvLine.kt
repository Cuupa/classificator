package com.cuupa.classificator.ui.csv

import com.cuupa.classificator.engine.extensions.Extension.substringBetween
import org.springframework.http.MediaType

class CsvLine(headlines: String, line: String) {

    val values: Map<String, String>

    val contentType: String
        get() = getValue("contentType") ?: MediaType.TEXT_PLAIN_VALUE

    val content: String
        get() {
            val value = getValue("content")
            if (value.isNullOrEmpty()) {
                throw IllegalArgumentException("No entry for content found")
            }
            return value
        }

    val tag: String?
        get() = getValue("tag")

    val topics: List<String>
        get() = getValuesAsList("topics")

    val senders: List<String>
        get() = getValuesAsList("senders")

    val metadata: List<String>
        get() = getValuesAsList("metadata")
            .map { it.replace(" : ", ":") }
            .map { it.replace(": ", ":") }
            .map { it.replace(" :", "") }

    private fun getValue(key: String) = values.getOrDefault(key.lowercase(), null)?.trim()

    private fun getValuesAsList(key: String): List<String> {
        val value = values.getOrDefault(key.lowercase(), null)
        if (value.isNullOrEmpty()) {
            return listOf()
        }
        return value.substringBetween("[", "]").split(",").map { it.trim() }
    }

    init {
        val headlinesList = headlines.split(";").map { it.lowercase() }.map { it.trim() }.filter { it.isNotEmpty() }

        val split = line.split(";").filter { it.isNotEmpty() }
        val map = mutableMapOf<String, String>()
        headlinesList.forEachIndexed { index, element -> map[element] = getValue(split, index) }

        values = map.toMap()
    }

    private fun getValue(split: List<String>, index: Int): String {
        return try {
            split[index].trim()
        } catch (e: ArrayIndexOutOfBoundsException) {
            ""
        }
    }
}
