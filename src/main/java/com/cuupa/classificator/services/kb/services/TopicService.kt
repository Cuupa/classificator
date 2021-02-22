package com.cuupa.classificator.services.kb.services

import com.cuupa.classificator.services.kb.SemanticResult
import com.cuupa.classificator.services.kb.Topic

class TopicService(private val topics: List<Topic>) {

    fun getTopics(text: String): MutableList<SemanticResult> {
        return topics.filter { it.match(text) }.map { SemanticResult(it.name) }.toMutableList()
    }
}
