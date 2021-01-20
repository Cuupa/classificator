package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import org.apache.commons.logging.LogFactory

class KnowledgeBaseExecutorService {

    private val senderService = SenderService()

    private val topicService = TopicService()

    fun submit(topics: List<Topic>, senderTokens: List<SenderToken>, text: String): List<SemanticResult> {
        val semanticResults = topicService.getTopics(topics, text)
        var mostFittingSender = senderService.getSender(senderTokens, text)

        if (mostFittingSender.isNullOrEmpty()) {
            LOG.debug("Looking for senders in metadata")
            mostFittingSender = senderService.findSenderFromMetadata(semanticResults, senderTokens, text)
        }

        semanticResults.forEach { it.sender = mostFittingSender ?: SenderToken.UNKNOWN }
        semanticResults.forEach { result ->
            result.metaData = result.metaData.distinctBy { it.value }.toMutableList()
        }

        LOG.debug(semanticResults)
        return semanticResults
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeBaseExecutorService::class.java)
    }
}