package com.cuupa.classificator.engine.services

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.domain.Sender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.logging.LogFactory

class KnowledgeBaseExecutorService(
    private val topicService: TopicService,
    private val senderService: SenderService,
    private val metadataService: MetadataService,
    private val languageDetectionService: LanguageDetectionService
) {

    fun submit(text: String): List<SemanticResult> {
        var semanticResults = mutableListOf<SemanticResult>()
        var mostFittingSender: String? = null
        var metadata = listOf<Metadata>()
        var languages = listOf<String>()

        runBlocking {
            val job = launch(Dispatchers.Default) {
                languages = languageDetectionService.getLanguages(text)

                val asyncMetadata = async {
                    metadataService.getMetadata(text, languages)
                }

                val asyncSenders = async {
                    senderService.getSender(text, languages)
                }

                val asyncTopics = async {
                    topicService.getTopics(text, languages)
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
        mostFittingSender?.let { sender ->

            val distinctMetadata = metadata.distinctBy { it.value }.filter { it.name != "sender" }.toMutableList()
            languages.forEach { distinctMetadata.add(Metadata("language", it)) }
            semanticResults.forEach {
                it.sender = sender
                it.metadata = distinctMetadata
            }
            if (semanticResults.isEmpty()) {
                semanticResults.add(SemanticResult(sender = sender, metadata = distinctMetadata))
            }
        }

        LOG.info(semanticResults)
        return semanticResults
    }

    companion object {
        private val LOG = LogFactory.getLog(KnowledgeBaseExecutorService::class.java)
    }
}