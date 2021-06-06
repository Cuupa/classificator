package com.cuupa.classificator.engine.services

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic

class TopicService(private val topics: List<Topic>) {

    fun getTopics(text: String) =
        topics.filter { it.match(text) }
            .map { SemanticResult(topic = it.name, sender = Sender.UNKNOWN, originalText = text) }
            .toMutableList()
}
