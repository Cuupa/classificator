package com.cuupa.classificator.ui

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.trainer.service.Document

data class TrainerClassifyProcess(
    var ids: List<String> = listOf(),
    var selected: Document = Document(),
    var selectedResult: Pair<String, List<SemanticResult>> = Pair("", listOf()),
    var topics: List<Topic> = listOf(),
    var sender: List<Sender> = listOf(),
    var metadata: List<Metadata> = listOf()
)
