package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Topic
import org.apache.juli.logging.LogFactory

class TopicService(private val topics: List<Topic>) {

    fun getTopics(text: String): MutableList<SemanticResult> {
        return topics.filter { it.match(text) }.map { SemanticResult(it.name) }.toMutableList()
    }

    companion object {
        private val LOG = LogFactory.getLog(TopicService::class.java)
    }
}
