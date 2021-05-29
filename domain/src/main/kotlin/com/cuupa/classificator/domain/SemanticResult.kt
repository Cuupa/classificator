package com.cuupa.classificator.domain

data class SemanticResult(
    val topic: String = Topic.OTHER,
    var sender: String = Sender.UNKNOWN,
    var metadata: List<Metadata> = listOf(),
    var originalText: String? = null
)
