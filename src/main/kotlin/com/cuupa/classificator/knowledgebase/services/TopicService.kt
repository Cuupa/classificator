package com.cuupa.classificator.knowledgebase.services

import com.cuupa.classificator.knowledgebase.resultobjects.SemanticResult
import com.cuupa.classificator.knowledgebase.resultobjects.Topic

class TopicService(private val topics: List<Topic>) {

    fun getTopics(text: String): MutableList<SemanticResult> {
        return topics.filter { it.match(text) }.map { SemanticResult(it.name) }.toMutableList()
    }
}
