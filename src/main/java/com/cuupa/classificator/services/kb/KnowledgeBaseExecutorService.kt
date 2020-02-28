package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import kotlinx.coroutines.*
import org.apache.commons.logging.LogFactory
import java.util.function.Consumer

class KnowledgeBaseExecutorService {

    fun submit(topics: List<Topic>, senderTokens: List<SenderToken>, text: String): List<SemanticResult> {
        var semanticResults: MutableList<SemanticResult> = mutableListOf()
        var mostFittingSender: String? = null
        runBlocking {
            val job = GlobalScope.launch {
                val asyncTopics: Deferred<MutableList<SemanticResult>> = GlobalScope.async { getTopics(topics, text) }
                val asyncSenders = GlobalScope.async { getSenders(senderTokens, text) }

                val senders = GlobalScope.async { getNumberOfOccurences(asyncSenders.await(), text) }.await()
                mostFittingSender = senders
                        .maxWith(compareBy { obj: SenderToken -> obj.countNumberOfOccurences() })!!.name

                semanticResults = asyncTopics.await()
            }
            while (job.isActive) {
                delay(10)
            }
        }

        if (semanticResults.isEmpty()) {
            LOG.debug("Found no matching Topics")
            val other = getMetadatasForTopicOther(topics, text)
            semanticResults.add(other)
        }
        if (mostFittingSender == null) {
            val sendersFromTopic: MutableList<Metadata> = mutableListOf()
            for ((_, _, metaData) in semanticResults) {
                sendersFromTopic.addAll(metaData
                        .filter { (name) -> SenderToken.SENDER == name })
            }
            sendersFromTopic.addAll(senderTokens
                    .map { e: SenderToken -> Metadata("sender", e.name!!) })

            mostFittingSender = sendersFromTopic
                    .map(Metadata::name).first()
        }

        // ToDo: Senders from metadata
        val finalMostFittingSender = mostFittingSender
        semanticResults.forEach(Consumer { result: SemanticResult -> result.sender = finalMostFittingSender })
        semanticResults.forEach(
                Consumer { (_, _, metaData) ->
                    val senderFound = metaData
                            .filter { (name) -> "sender" == name }
                            .any { (_, value) -> finalMostFittingSender == value }
                    if (senderFound) {
                        metaData.add(Metadata("sender", finalMostFittingSender!!))
                    }
                })

        semanticResults.forEach(Consumer { result ->
            result.metaData = result.metaData.distinctBy { it.value }.toMutableList()
        })
        LOG.debug(semanticResults)
        return semanticResults
    }

    /**
     * If no topic is found we still want to get the metadata
     *
     * @param text The text to extract Metadata from
     * @return returns SemanticResult.OTHER with metadata
     */
    private fun getMetadatasForTopicOther(topics: List<Topic>, text: String): SemanticResult {
        val result = topics
                .map { e: Topic -> SemanticResult(Topic.OTHER, e.getMetaData(text)) }

                .filter { (_, _, metaData) -> metaData.isNotEmpty() }
                .firstOrNull()

        return result ?: SemanticResult(Topic.OTHER, mutableListOf())
    }

    private fun getSenders(senders: List<SenderToken>, text: String): List<SenderToken> {
        return senders
                .filter { e: SenderToken -> e.match(text) }
    }

    private fun getNumberOfOccurences(senders: List<SenderToken>,
                                      text: String): List<SenderToken> {
        senders.forEach(Consumer { senderToken: SenderToken -> senderToken.countNumberOfOccurences(text) })
        return senders
    }

    private fun getTopics(topics: List<Topic>, text: String): MutableList<SemanticResult> {
        return topics
                .filter { e: Topic -> e.match(text) }
                .map { e: Topic -> SemanticResult(e.name!!, e.getMetaData(text)) }.toMutableList()
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeBaseExecutorService::class.java)
    }
}