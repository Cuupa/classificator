package com.cuupa.classificator.trainer.services.statistics

import com.cuupa.classificator.trainer.services.Document

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class Precision {

    fun getPrecision(all: List<Document>, name: String, type: MeasureType): PrecisionResult {
        return when (type) {
            MeasureType.TOPIC -> getTopicPrecision(name, all)
            MeasureType.SENDER -> getSenderPrecision(name, all)
            MeasureType.METADATA -> getMetadataPrecision(name, all)
            else -> PrecisionResult(MeasureType.UNDEFINED)
        }
    }

    private fun getMetadataPrecision(name: String, all: List<Document>): PrecisionResult {
        val truePositive = all
            .filter { it.actualMetadata.contains(name) }
            .filter { it.expectedMetadata.contains(name) }
            .size.toDouble()

        val trueNegative = all
            .filter { !it.actualMetadata.contains(name) }
            .filter { !it.expectedMetadata.contains(name) }
            .size.toDouble()

        return PrecisionResult(MeasureType.METADATA).apply { setPrecision(truePositive, trueNegative) }
    }

    private fun getSenderPrecision(name: String, all: List<Document>): PrecisionResult {
        val truePositive = all
            .filter { it.actualSenders.contains(name) }
            .filter { it.expectedSenders.contains(name) }
            .size.toDouble()

        val trueNegative = all
            .filter { !it.actualSenders.contains(name) }
            .filter { !it.expectedSenders.contains(name) }
            .size.toDouble()

        return PrecisionResult(MeasureType.SENDER).apply { setPrecision(truePositive, trueNegative) }
    }

    private fun getTopicPrecision(name: String, all: List<Document>): PrecisionResult {
        val truePositive = all
            .filter { it.actualTopics.contains(name) }
            .filter { it.expectedTopics.contains(name) }
            .size.toDouble()

        val trueNegative = all
            .filter { !it.actualTopics.contains(name) }
            .filter { !it.expectedTopics.contains(name) }
            .size.toDouble()

        return PrecisionResult(MeasureType.TOPIC).apply { setPrecision(truePositive, trueNegative) }
    }
}