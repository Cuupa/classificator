package com.cuupa.classificator.engine.services

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic

class TopicService(private val topics: List<Topic>) {

    fun getTopics(text: String, languages: List<String>) =
        topics.filter { it.match(text) }.map { SemanticResult(it.name, Sender.UNKNOWN) }.toMutableList()
}
