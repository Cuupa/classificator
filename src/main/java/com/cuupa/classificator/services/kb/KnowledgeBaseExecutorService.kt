package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import kotlinx.coroutines.*
import org.apache.commons.logging.LogFactory

class KnowledgeBaseExecutorService {

    fun submit(topics: List<Topic>, senderTokens: List<SenderToken>, text: String): List<SemanticResult> {
        var semanticResults = mutableListOf<SemanticResult>()
        var mostFittingSender: String? = null
        runBlocking {
            val job = GlobalScope.launch {
                val asyncTopics = GlobalScope.async { getTopics(topics, text) }
                val asyncSenders = GlobalScope.async { getSenders(senderTokens, text) }

                val senders = withContext(Dispatchers.Default) { getNumberOfOccurences(asyncSenders.await(), text) }
                mostFittingSender = senders.maxWith(compareBy { it.countNumberOfOccurences() })?.name
                semanticResults = asyncTopics.await()
            }
            while (job.isActive) {
                delay(10)
            }
        }

        if (semanticResults.isEmpty()) {
            LOG.debug("Found no matching Topics")
            semanticResults.add(getMetadatasForTopicOther(topics, text))
        }

        if (mostFittingSender == null) {
            val sendersFromTopic = mutableListOf<Metadata>()
            for ((_, _, metaData) in semanticResults) {
                sendersFromTopic.addAll(metaData.filter { (name) -> SenderToken.SENDER == name })
            }

            sendersFromTopic.addAll(senderTokens.map { Metadata("sender", it.name) })

            val filteredText = sendersFromTopic.filter { text.contains(it.value) }.groupingBy { it.value }.eachCount()
            val sortedBy = filteredText.entries.sortedBy { it.value }
            // Need more than one occurence to bypass simple mentions like on sicknotes
            if (sortedBy.isNotEmpty() && sortedBy.first().value > 1) {
                mostFittingSender = sortedBy.first().key
            }
        }

        if (mostFittingSender.isNullOrEmpty()) {
            mostFittingSender = SenderToken.UNKNOWN
        }

        val finalMostFittingSender = mostFittingSender!!
        semanticResults.forEach { it.sender = finalMostFittingSender }
        semanticResults.forEach { (_, _, metaData) ->
            findAndSetSender(metaData, finalMostFittingSender)
        }

        semanticResults.forEach { result ->
            result.metaData = result.metaData.distinctBy { it.value }.toMutableList()
        }
        LOG.debug(semanticResults)
        return semanticResults
    }

    private fun findAndSetSender(metaData: MutableList<Metadata>, finalMostFittingSender: String) {
        val senderFound = metaData.filter { (name) -> "sender" == name }
                .any { (_, value) -> finalMostFittingSender == value }
        if (senderFound) {
            metaData.add(Metadata("sender", finalMostFittingSender))
        }
    }

    /**
     * If no topic is found we still want to get the metadata
     *
     * @param text The text to extract Metadata from
     * @return returns SemanticResult.OTHER with metadata
     */
    private fun getMetadatasForTopicOther(topics: List<Topic>, text: String): SemanticResult {
        val result = topics.map { SemanticResult(Topic.OTHER, it.getMetaData(text)) }
                .firstOrNull { (_, _, metaData) -> metaData.isNotEmpty() }

        return result ?: SemanticResult(Topic.OTHER, mutableListOf())
    }

    private fun getSenders(senders: List<SenderToken>, text: String): List<SenderToken> {
        return senders.filter { it.match(text) }
    }

    // EVIL. Works on the original list and therefore altered the senderdefinition from knowledgebase
    private fun getNumberOfOccurences(senders: List<SenderToken>, text: String): List<SenderToken> {
        senders.forEach { senderToken: SenderToken -> senderToken.countNumberOfOccurences(text) }
        return senders
    }

    private fun getTopics(topics: List<Topic>, text: String): MutableList<SemanticResult> {
        return topics.filter { it.match(text) }.map { SemanticResult(it.name, it.getMetaData(text)) }.toMutableList()
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeBaseExecutorService::class.java)
    }
}