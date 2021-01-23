package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Topic

class KnowledgeManager(
    private val knowledgeBase: KnowledgeBase,
    private val knowledgeBaseExecutorService: KnowledgeBaseExecutorService
) {

    fun getResults(text: String): List<SemanticResult> {
        return knowledgeBaseExecutorService.submit(text)
    }

    fun manualParse(parseTopic: Topic) {
        knowledgeBase.topicList = mutableListOf(parseTopic)
    }
}