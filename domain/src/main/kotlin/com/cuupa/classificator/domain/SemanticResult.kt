package com.cuupa.classificator.domain

data class SemanticResult(val topicName: String = Topic.OTHER, var sender: String = Sender.UNKNOWN, var metadata: List<Metadata> = listOf())
