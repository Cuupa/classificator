package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.services.kb.services.knowledgebase.KnowledgeBase

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

    fun getVersion(): String {
        return knowledgeBase.knowledgeBaseMetadata.version
    }
}