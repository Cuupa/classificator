package com.cuupa.classificator.knowledgebase.services.dataExtraction

import java.util.regex.Pattern

abstract class Extract(val pattern: Pattern) {
    abstract fun normalize(value: String): String

    open fun get(text: String, textBeforeToken: String, textAfterToken: String): List<Pair<String, String>> {
        val matcher = pattern.matcher(text)

        val value = mutableListOf<Pair<String, String>>()
        while (matcher.find()) {
            val normalizedValue = normalize(matcher.group())
            value.add(Pair(textBeforeToken + normalizedValue + textAfterToken, normalizedValue))
        }
        return value
    }
}