package com.cuupa.classificator.ui

import com.cuupa.classificator.domain.*

data class TrainerProcess(
    var version: String = "",
    var topics: List<Topic> = listOf(),
    var sender: List<Sender> = listOf(),
    var metadata: List<Metadata> = listOf(),
    var regex: List<Regex> = listOf(),
    var result: List<SemanticResult> = listOf(),
    val selected: SemanticResultData = Topic()
)
