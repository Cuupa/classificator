package com.cuupa.classificator.knowledgebase

import com.cuupa.classificator.knowledgebase.resultobjects.SemanticResult
import com.cuupa.classificator.knowledgebase.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.knowledgebase.services.kb.KnowledgeBase

class KnowledgeManager(
    private val knowledgeBase: KnowledgeBase,
    private val knowledgeBaseExecutorService: KnowledgeBaseExecutorService
) {

    fun getResults(text: String): List<SemanticResult> {
        return knowledgeBaseExecutorService.submit(text)
    }

    fun getVersion(): String {
        return knowledgeBase.knowledgeBaseMetadata.version
    }
}