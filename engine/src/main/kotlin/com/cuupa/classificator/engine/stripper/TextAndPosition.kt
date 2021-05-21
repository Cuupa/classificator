package com.cuupa.classificator.engine.stripper

class TextAndPosition {

    var value = ""
        private set
    var x = 0f
    private var toX = 0f
    var y = 0f
    private var height = 0f

    fun add(value: String, xDirAdj: Float, width: Float, yDirAdj: Float,
            heightDir: Float) {
        this.value += value
        if (xDirAdj < x || x == 0f) {
            x = xDirAdj
        }
        if (xDirAdj > toX) {
            toX = xDirAdj + width
        }
        y = yDirAdj
        height = heightDir
    }

    val width: Float
        get() = toX - x

}