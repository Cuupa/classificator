package com.cuupa.classificator.knowledgebase.services.dataExtraction


abstract class Extract(val regex: Regex) {

    abstract fun normalize(value: String): String

    open fun get(text: String, textBeforeToken: String, textAfterToken: String): List<Pair<String, String>> {
        return regex.findAll(text).map {
            val normalized = normalize(it.value)
            Pair(textBeforeToken + normalized + textAfterToken, normalized)
        }.toList()
    }
}