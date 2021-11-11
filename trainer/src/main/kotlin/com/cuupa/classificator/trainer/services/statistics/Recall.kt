package com.cuupa.classificator.trainer.services.statistics

import com.cuupa.classificator.trainer.services.Document

class Recall {
    fun getRecall(all: List<Document>, name: String, type: MeasureType): RecallResult {
        return when (type) {
            MeasureType.TOPIC -> getTopicRecall(name, all)
            MeasureType.SENDER -> getSenderRecall(name, all)
            MeasureType.METADATA -> getMetadataRecall(name, all)
            else -> RecallResult(MeasureType.UNDEFINED)
        }
    }

    private fun getTopicRecall(name: String, all: List<Document>): RecallResult {
        val truePositive = all
            .filter { it.actualTopics.contains(name) }
            .filter { it.expectedTopics.contains(name) }
            .size.toDouble()

        val falseNegative = all
            .filter { !it.actualTopics.contains(name) }
            .filter { it.expectedTopics.contains(name) }
            .size.toDouble()

        return RecallResult(MeasureType.TOPIC).apply { setRecall(truePositive, falseNegative) }
    }

    private fun getSenderRecall(name: String, all: List<Document>): RecallResult {
        val truePositive = all
            .filter { it.actualSenders.contains(name) }
            .filter { it.expectedSenders.contains(name) }
            .size.toDouble()

        val falseNegative = all
            .filter { it.actualSenders.contains(name) }
            .filter { !it.expectedSenders.contains(name) }
            .size.toDouble()

        return RecallResult(MeasureType.SENDER).apply { setRecall(truePositive, falseNegative) }
    }

    private fun getMetadataRecall(name: String, all: List<Document>): RecallResult {
        val truePositive = all
            .filter { it.actualMetadata.contains(name) }
            .filter { it.expectedMetadata.contains(name) }
            .size.toDouble()

        val falseNegative = all
            .filter { it.actualMetadata.contains(name) }
            .filter { !it.expectedMetadata.contains(name) }
            .size.toDouble()

        return RecallResult(MeasureType.METADATA).apply { setRecall(truePositive, falseNegative) }
    }
}
