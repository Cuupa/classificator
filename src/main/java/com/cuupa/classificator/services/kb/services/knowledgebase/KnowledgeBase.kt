package com.cuupa.classificator.services.kb.services.knowledgebase

import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken

class KnowledgeBase {

    var metadataList: List<MetaDataToken> = listOf()
    var topicList: List<Topic> = listOf()
    var sendersList: List<SenderToken> = listOf()

    fun clear() {
        topicList = listOf()
        sendersList = listOf()
        metadataList = listOf()
    }
}