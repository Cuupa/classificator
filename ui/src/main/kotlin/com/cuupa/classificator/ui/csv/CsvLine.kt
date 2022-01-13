package com.cuupa.classificator.ui.csv

class CsvLine(headlines: String, line: String) {

    val values: Map<String, String>

    val contentType: String
        get() {
            val value = getValue("contentType")
            if (value.isEmpty()) {
                throw IllegalArgumentException("No entry for contentType found")
            }
            return value
        }

    val content: String
        get() {
            val value = getValue("content")
            if (value.isEmpty()) {
                throw IllegalArgumentException("No entry for content found")
            }
            return value
        }

    val tag: String
        get() {
            val value = getValue("content")
            if (value.isEmpty()) {
                throw IllegalArgumentException("No entry for content found")
            }
            return value
        }

    private fun getValue(key: String) = values.getOrDefault(key.lowercase(), "").trim()

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
