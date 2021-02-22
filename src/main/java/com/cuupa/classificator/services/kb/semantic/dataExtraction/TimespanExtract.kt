package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

class TimespanExtract(regex: String) : Extract(Pattern.compile(regex.trim(), Pattern.CASE_INSENSITIVE)) {

    override fun normalize(value: String): String {
        return value
    }

    override fun get(
        text: String,
        textBeforeToken: String,
        textAfterToken: String
    ): List<Pair<String, String>> {
        val matcher = pattern.matcher(text)

        val value = mutableListOf<Pair<String, String>>()
        val dates = mutableListOf<String>()
        while (matcher.find()) {
            val normalizedValue = normalize(matcher.group())
            dates.add(normalizedValue)
            value.add(Pair(textBeforeToken + normalizedValue + textAfterToken, normalizedValue))
        }
        val finalValue = mutableListOf<Pair<String, String>>()
        value.forEach { content ->
            dates.forEach { date ->
                finalValue.add(
                    Pair(
                        content.first + date,
                        content.second + " - " + date
                    )
                )
            }
        }
        return finalValue
    }

    companion object {
        const val name = "[TIMESPAN]"
    }

}