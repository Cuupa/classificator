package com.cuupa.classificator.engine.knowledgebase

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.engine.knowledgebase.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.engine.knowledgebase.services.kb.KnowledgeBase

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