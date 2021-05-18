package com.cuupa.classificator.knowledgebase.services.kb

import com.cuupa.classificator.knowledgebase.resultobjects.Sender
import com.cuupa.classificator.knowledgebase.resultobjects.Topic
import com.cuupa.classificator.knowledgebase.services.token.MetaDataToken

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