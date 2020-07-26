package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Topic
import org.apache.commons.logging.LogFactory

class KnowledgeManager(private val knowledgeBaseInitiator: KnowledgeBaseInitiator,
                       private val knowledgeBaseExecutorService: KnowledgeBaseExecutorService) {

    private var knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase()

    fun reloadKB() {
        knowledgeBase.clear()
        knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase()
    }

    fun getResults(text: String): List<SemanticResult> {
        return knowledgeBaseExecutorService.submit(knowledgeBase.topicList, knowledgeBase.senders, text)
    }

    fun manualParse(parseTopic: Topic) {
        val topics: MutableList<Topic> = mutableListOf()
        topics.add(parseTopic)
        knowledgeBase.topicList = topics
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeManager::class.java)
    }
}