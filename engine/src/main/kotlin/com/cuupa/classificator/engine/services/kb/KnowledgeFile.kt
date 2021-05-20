package com.cuupa.classificator.engine.services.kb

import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.services.token.MetaDataToken

class KnowledgeFile {

    var regex = mutableListOf<Pair<String, String>>()
    var senders = mutableListOf<Sender>()
    var metadata = mutableListOf<MetaDataToken>()
    var topics = mutableListOf<Topic>()
    lateinit var kbMetadata: KnowledgeBaseMetadata

    fun create(): KnowledgeBase {
        return KnowledgeBase().apply {
            metadataList = metadata
            knowledgeBaseMetadata = kbMetadata
            topicList = topics
            sendersList = senders
            metadataList.forEach { metadata ->
                metadata.regexContent = regex.filter { metadata.name.contains(it.first, true) }
            }
            metadataList.forEach { it.regexContent.ifEmpty { it.regexContent = regex } }
        }
    }
}
