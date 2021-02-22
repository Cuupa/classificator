package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Topic

class KnowledgeManager(private val knowledgeBaseInitiator: KnowledgeBaseInitiator,
                       private val knowledgeBaseExecutorService: KnowledgeBaseExecutorService) {

    private var knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase()

    fun reloadKB() {
        knowledgeBase.clear()
        knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase()
    }

    fun getResults(text: String): List<SemanticResult> {
        return knowledgeBaseExecutorService.submit(knowledgeBase.topicList.toList(),
                                                   knowledgeBase.senders.toList(),
                                                   text)
    }

    fun manualParse(parseTopic: Topic) {
        knowledgeBase.topicList = mutableListOf(parseTopic)
    }
}