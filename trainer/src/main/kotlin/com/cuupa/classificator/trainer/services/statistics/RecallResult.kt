package com.cuupa.classificator.trainer.services.statistics

class RecallResult(type: MeasureType? = MeasureType.UNDEFINED) {

    var recall: Double = 0.0
        private set

    fun setRecall(tp: Double, fn: Double) {
        val denominator = tp + fn
        recall = if (denominator == 0.0) {
            0.0
        } else {
            tp / denominator
        }
    }
}
