package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic

class KnowledgeBase {

    var topicList: MutableList<Topic> = mutableListOf()
    var senders: MutableList<SenderToken> = mutableListOf()

    fun clear() {
        topicList.clear()
        senders.clear()
    }
}