package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.SenderToken
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
                val asyncTopics = async {
                    LOG.info("I'm Thread ${Thread.currentThread()}")
                    topicService.getTopics(text)
                }
                val asyncSenders = async {
                    LOG.info("I'm Thread ${Thread.currentThread()}")
                    senderService.getSender(text)
                }

                val asyncMetadata = async {
                    LOG.info("I'm Thread ${Thread.currentThread()}")
                    metadataService.getMetadata(text)
                }

                metadata = asyncMetadata.await()
                mostFittingSender = asyncSenders.await()
                if (mostFittingSender.isNullOrEmpty()) {
                    mostFittingSender = senderService.findSenderFromMetadata(metadata, text)
                }

                semanticResults = asyncTopics.await()
            }
            job.join()
        }

        val distinctMetadata = metadata.distinctBy { it.value }.toList()
        semanticResults.forEach {
            it.sender = mostFittingSender ?: SenderToken.UNKNOWN
            it.metaData = distinctMetadata
        }
        LOG.debug(semanticResults)
        return semanticResults
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeBaseExecutorService::class.java)
    }
}