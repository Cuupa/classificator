package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Topic
import org.apache.commons.logging.LogFactory

class KnowledgeManager(private val knowledgeBaseInitiator: KnowledgeBaseInitiator,
                       private val knowledgeBaseExecutorService: KnowledgeBaseExecutorService) {
    private var knowledgeBase: KnowledgeBase

    fun reloadKB() {
        knowledgeBase.clear()
        knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase()
    }

    fun getResults(text: String?): List<SemanticResult> {
        if (text.isNullOrBlank()) {
            return listOf()
        }
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

    init {
        knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase()
    }
}