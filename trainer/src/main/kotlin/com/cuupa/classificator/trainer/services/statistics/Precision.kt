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
        return if (name.isEmpty()) {
            val truePositive = all
                .filter { it.actualMetadata.isEmpty() }
                .filter { it.expectedMetadata.isEmpty() }
                .size.toDouble()

            val trueNegative = all
                .filter { it.actualMetadata.isNotEmpty() }
                .filter { it.expectedMetadata.isNotEmpty() }
                .size.toDouble()
            PrecisionResult(MeasureType.METADATA).apply { setPrecision(truePositive, trueNegative) }
        } else {
            val truePositive = all
                .filter { it.actualMetadata.contains(name) }
                .filter { it.expectedMetadata.contains(name) }
                .size.toDouble()

            val trueNegative = all
                .filter { !it.actualMetadata.contains(name) }
                .filter { !it.expectedMetadata.contains(name) }
                .size.toDouble()

            PrecisionResult(MeasureType.METADATA).apply { setPrecision(truePositive, trueNegative) }
        }
    }

    private fun getSenderPrecision(name: String, all: List<Document>): PrecisionResult {
        return if (name.isEmpty()) {
            val truePositive = all
                .filter { it.actualSenders.isEmpty() }
                .filter { it.expectedSenders.isEmpty() }
                .size.toDouble()

            val trueNegative = all
                .filter { it.actualSenders.isNotEmpty() }
                .filter { it.expectedSenders.isNotEmpty() }
                .size.toDouble()
            PrecisionResult(MeasureType.SENDER).apply { setPrecision(truePositive, trueNegative) }
        } else {
            val truePositive = all
                .filter { it.actualSenders.contains(name) }
                .filter { it.expectedSenders.contains(name) }
                .size.toDouble()

            val trueNegative = all
                .filter { !it.actualSenders.contains(name) }
                .filter { !it.expectedSenders.contains(name) }
                .size.toDouble()

            PrecisionResult(MeasureType.SENDER).apply { setPrecision(truePositive, trueNegative) }
        }
    }

    private fun getTopicPrecision(name: String, all: List<Document>): PrecisionResult {
        return if (name.isEmpty()) {
            val truePositive = all
                .filter { it.actualTopics.isEmpty() }
                .filter { it.expectedTopics.isEmpty() }
                .size.toDouble()

            val trueNegative = all
                .filter { it.actualTopics.isNotEmpty() }
                .filter { it.expectedTopics.isNotEmpty() }
                .size.toDouble()
            PrecisionResult(MeasureType.TOPIC).apply { setPrecision(truePositive, trueNegative) }
        } else {
            val truePositive = all
                .filter { it.actualTopics.contains(name) }
                .filter { it.expectedTopics.contains(name) }
                .size.toDouble()

            val trueNegative = all
                .filter { !it.actualTopics.contains(name) }
                .filter { !it.expectedTopics.contains(name) }
                .size.toDouble()
            PrecisionResult(MeasureType.TOPIC).apply { setPrecision(truePositive, trueNegative) }
        }
    }
}