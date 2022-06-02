package com.cuupa.classificator.trainer.services.statistics

class PrecisionResult(var measureType: MeasureType? = MeasureType.UNDEFINED) {

    var precision: Double = 0.0
        private set

    fun setPrecision(tp: Double, fp: Double) {
        val denominator = tp + fp
        precision = if (denominator == 0.0) {
            0.0
        } else {
            tp / denominator
        }
    }
}
