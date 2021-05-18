package com.cuupa.classificator.engine.knowledgebase.services

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.domain.Topic

class TopicService(private val topics: List<Topic>) {

    fun getTopics(text: String): MutableList<SemanticResult> {
        return topics.filter { it.match(text) }.map { SemanticResult(it.name) }.toMutableList()
    }
}
