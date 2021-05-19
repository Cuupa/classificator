package com.cuupa.classificator.engine

import com.cuupa.classificator.engine.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.engine.services.kb.KnowledgeBase

class KnowledgeManager(
    private val knowledgeBase: KnowledgeBase,
    private val knowledgeBaseExecutorService: KnowledgeBaseExecutorService
) {

    fun getResults(text: String) = knowledgeBaseExecutorService.submit(text)

    fun getVersion() = knowledgeBase.knowledgeBaseMetadata.version
}