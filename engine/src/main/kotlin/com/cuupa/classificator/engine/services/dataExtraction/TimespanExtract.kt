package com.cuupa.classificator.engine.services.dataExtraction

class TimespanExtract(regex: String) : Extract(Regex(regex.trim(), RegexOption.IGNORE_CASE)) {

    override fun normalize(value: String) = value

    override fun get(
        text: String,
        textBeforeToken: String,
        textAfterToken: String
    ): List<Pair<String, String>> {
        val dates = mutableListOf<String>()
        val value = regex.findAll(text).map {
            val normalized = normalize(it.value)
            Pair(textBeforeToken + normalized + textAfterToken, normalized)
        }.toList()
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