package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.Sender

data class SemanticResult(val topicName: String, var sender: String, var metaData: List<Metadata>) {

    constructor(topicName: String, metaData: MutableList<Metadata>) : this(topicName, Sender.UNKNOWN, metaData)

    constructor(topicName: String) : this(topicName, Sender.UNKNOWN, mutableListOf())
}