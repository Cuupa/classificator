package com.cuupa.classificator.engine.services.kb

import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.services.token.MetaDataToken

class KnowledgeBase {

    var knowledgeBaseMetadata = KnowledgeBaseMetadata()
    var metadataList = listOf<MetaDataToken>()
    var topicList = listOf<Topic>()
    var sendersList = listOf<Sender>()

    fun clear() {
        knowledgeBaseMetadata.version = ""
        topicList = listOf()
        sendersList = listOf()
        metadataList = listOf()
    }

    fun isValid(): Boolean {
        return knowledgeBaseMetadata.version.isNotBlank()
    }
}