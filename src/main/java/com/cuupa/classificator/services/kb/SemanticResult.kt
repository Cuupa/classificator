package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Metadata

data class SemanticResult(val topicName: String, var sender: String?, var metaData: MutableList<Metadata>) {

    constructor(topicName: String,
                metaData: MutableList<Metadata>) : this(topicName, null, metaData)
}