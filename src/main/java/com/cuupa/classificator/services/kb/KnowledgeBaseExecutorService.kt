package com.cuupa.classificator.services.kb

import com.cuupa.classificator.constants.StringConstants
import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import com.cuupa.classificator.services.kb.semantic.token.CountToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.logging.LogFactory

class KnowledgeBaseExecutorService {

    fun submit(topics: List<Topic>, senderTokens: List<SenderToken>, text: String): List<SemanticResult> {
        var semanticResults = mutableListOf<SemanticResult>()
        var mostFittingSender: String? = null
        runBlocking {
            val job = launch(Dispatchers.Default) {
                val asyncTopics = async {
                    println("I'm Thread ${Thread.currentThread()}")
                    getTopics(topics, text)
                }
                val asyncSenders = async {
                    println("I'm Thread ${Thread.currentThread()}")
                    getSenders(senderTokens, text)
                }

                val senders = getNumberOfOccurrences(asyncSenders.await(), text)
                mostFittingSender = senders.maxWith(compareBy { it.countNumberOfOccurences() })?.name
                semanticResults = asyncTopics.await()
            }
            job.join()
        }

        if (semanticResults.isEmpty()) {
            LOG.debug("Found no matching Topics")
            semanticResults.add(getMetadatasForTopicOther(topics, text))
        }

        if (mostFittingSender.isNullOrEmpty()) {
            LOG.debug("Looking for senders in metadata")
            mostFittingSender = findSenderFromMetadata(semanticResults, senderTokens, text)
        }

        semanticResults.forEach { it.sender = mostFittingSender ?: SenderToken.UNKNOWN }

        semanticResults.forEach { result ->
            result.metaData = result.metaData.distinctBy { it.value }.toMutableList()
        }
        LOG.debug(semanticResults)
        return semanticResults
    }

    private fun findSenderFromMetadata(semanticResults: MutableList<SemanticResult>, senderTokens: List<SenderToken>,
                                       text: String): String {
        val sendersFromTopic = mutableListOf<Metadata>()
        for ((_, _, metaData) in semanticResults) {
            sendersFromTopic.addAll(metaData.filter { (name) -> StringConstants.sender == name })
        }

        sendersFromTopic.addAll(senderTokens.map { Metadata(StringConstants.sender, it.name) })

        val filteredText = sendersFromTopic.filter { text.contains(it.value) }
        val mutableMapOf = mutableMapOf<String, Int>()
        filteredText.forEach(weightSenders(mutableMapOf, text))
        return mutableMapOf.filter(lessOrEqualFiveSpaces()).maxBy { it.value }?.key ?: SenderToken.UNKNOWN
    }

    private fun lessOrEqualFiveSpaces(): (Map.Entry<String, Int>) -> Boolean = { it.key.count { char -> ' ' == char } <= 5 }

    private fun weightSenders(mutableMapOf: MutableMap<String, Int>, text: String): (Metadata) -> Unit = {
        mutableMapOf[it.value] = CountToken().countOccurences(it.value, text) * it.value.length
    }

    /**
     * If no topic is found we still want to get the metadata
     *
     * @param text The text to extract Metadata from
     * @return returns SemanticResult.OTHER with metadata
     */
    private fun getMetadatasForTopicOther(topics: List<Topic>, text: String): SemanticResult {
        return topics.map { SemanticResult(Topic.OTHER, it.getMetaData(text)) }
            .firstOrNull { (_, _, it) -> it.isNotEmpty() } ?: SemanticResult(Topic.OTHER, mutableListOf())

    }

    private fun getSenders(senders: List<SenderToken>, text: String): List<SenderToken> {
        return senders.filter { it.match(text) }
    }

    private fun getNumberOfOccurrences(senders: List<SenderToken>, text: String): List<SenderToken> {
        senders.forEach { it.countNumberOfOccurences(text) }
        return senders
    }

    private fun getTopics(topics: List<Topic>, text: String): MutableList<SemanticResult> {
        return topics.filter { it.match(text) }.map(toSemanticResult(text)).toMutableList()
    }

    private fun toSemanticResult(text: String): (Topic) -> SemanticResult = {
        SemanticResult(it.name, it.getMetaData(text))
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeBaseExecutorService::class.java)
    }
}