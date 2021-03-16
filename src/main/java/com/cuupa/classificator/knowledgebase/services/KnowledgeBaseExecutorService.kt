package com.cuupa.classificator.knowledgebase.services

import com.cuupa.classificator.knowledgebase.resultobjects.Metadata
import com.cuupa.classificator.knowledgebase.resultobjects.SemanticResult
import com.cuupa.classificator.knowledgebase.resultobjects.Sender
import com.cuupa.classificator.knowledgebase.resultobjects.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.logging.LogFactory

class KnowledgeBaseExecutorService(
    private val topicService: TopicService,
    private val senderService: SenderService,
    private val metadataService: MetadataService
) {

    fun submit(text: String): List<SemanticResult> {
        var semanticResults = mutableListOf<SemanticResult>()
        var mostFittingSender: String? = null
        var metadata = listOf<Metadata>()
        runBlocking {
            val job = launch(Dispatchers.Default) {
                val asyncMetadata = async {
                    metadataService.getMetadata(text)
                }

                val asyncSenders = async {
                    senderService.getSender(text)
                }

                val asyncTopics = async {
                    topicService.getTopics(text)
                }

                mostFittingSender = asyncSenders.await()
                metadata = asyncMetadata.await()
                if (mostFittingSender.isNullOrEmpty()) {
                    mostFittingSender = senderService.findSenderFromMetadata(metadata, text)
                }

                semanticResults = asyncTopics.await()
            }
            job.join()
        }

        if (mostFittingSender.isNullOrEmpty()) {
            mostFittingSender = Sender.UNKNOWN
        }
        val distinctMetadata = metadata.distinctBy { it.value }.filter { it.name != "sender" }.toList()
        semanticResults.forEach {
            it.sender = mostFittingSender!!
            it.metaData = distinctMetadata
        }
        if (semanticResults.isEmpty()) {
            semanticResults.add(SemanticResult(Topic.OTHER, mostFittingSender!!, distinctMetadata))
        }
        LOG.info(semanticResults)
        return semanticResults
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeBaseExecutorService::class.java)
    }
}