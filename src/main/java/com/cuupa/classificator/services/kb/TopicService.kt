package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Topic
import org.apache.juli.logging.LogFactory

class TopicService {

    private val LOG = LogFactory.getLog(TopicService::class.java)

    private val mapper = SemanticResultMapper()

    fun getTopics(topics: List<Topic>, text: String): MutableList<SemanticResult> {
        val semanticResults = topics.filter { it.match(text) }.map(mapper.toSemanticResult(text)).toMutableList()
        if (semanticResults.isEmpty()) {
            LOG.debug("Found no matching Topics")
            semanticResults.add(getMetadatasForTopicOther(topics, text))
        }
        return semanticResults
    }

    /**
     * If no topic is found we still want to get the metadata
     *
     * @param text The text to extract Metadata from
     * @return returns SemanticResult.OTHER with metadata
     */
    private fun getMetadatasForTopicOther(topics: List<Topic>, text: String): SemanticResult {
        return topics.map { SemanticResult(Topic.OTHER, it.getMetaData(text)) }
            .firstOrNull { (_, _, it) -> it.isNotEmpty() } ?: SemanticResult(Topic.OTHER)
    }
}
