package com.cuupa.classificator.services.kb.services.knowledgebase

import com.cuupa.classificator.services.kb.semantic.Sender
import com.cuupa.classificator.services.kb.semantic.Topic
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken

class KnowledgeBase {

    var knowledgeBaseMetadata = KnowledgeBaseMetadata()
    var metadataList: List<MetaDataToken> = listOf()
    var topicList: List<Topic> = listOf()
    var sendersList: List<Sender> = listOf()

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