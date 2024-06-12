package com.cuupa.classificator.trainer.services.statistics

import com.cuupa.classificator.domain.Metadata
import com.cuupa.classificator.domain.SemanticResultData
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic

enum class MeasureType {

    TOPIC,
    SENDER,
    METADATA,
    UNDEFINED;

    companion object {

        fun getFor(data: SemanticResultData?): MeasureType {
            return when (data) {
                is Topic -> TOPIC
                is Sender -> SENDER
                is Metadata -> METADATA
                else -> UNDEFINED
            }
        }
    }
}
