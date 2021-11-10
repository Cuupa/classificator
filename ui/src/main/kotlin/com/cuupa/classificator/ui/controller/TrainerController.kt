package com.cuupa.classificator.ui.controller

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.engine.ClassificatorImplementation
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.TextExtractor
import com.cuupa.classificator.engine.services.token.MetaDataToken
import com.cuupa.classificator.trainer.services.Trainer

open class TrainerController(
    val classificator: ClassificatorImplementation,
    val manager: KnowledgeManager,
    val trainer: Trainer,
    val textExtractor: TextExtractor
) {

    protected fun List<MetaDataToken>.toMetadata(): List<Metadata> {
        return this.map {
            Metadata().apply {
                name = it.name
                tokenList = it.tokenList
                regexContent = it.regexContent
            }
        }
    }
}
