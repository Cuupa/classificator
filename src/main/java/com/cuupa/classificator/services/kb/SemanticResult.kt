package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.SenderToken

data class SemanticResult(val topicName: String, var sender: String, var metaData: MutableList<Metadata>) {

    constructor(topicName: String, metaData: MutableList<Metadata>) : this(topicName, SenderToken.UNKNOWN, metaData)
}