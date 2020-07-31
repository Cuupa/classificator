package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic

class KnowledgeBase {

    var topicList: List<Topic> = listOf()
    var senders: List<SenderToken> = listOf()

    fun clear() {
        topicList = listOf()
        senders = listOf()
    }
}