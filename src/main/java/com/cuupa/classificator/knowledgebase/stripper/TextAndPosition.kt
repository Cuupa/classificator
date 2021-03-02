package com.cuupa.classificator.knowledgebase.stripper

import org.apache.logging.log4j.util.Strings

class TextAndPosition {

    var value = Strings.EMPTY
        private set
    var x = 0f
    private var toX = 0f
    var y = 0f
    var height = 0f

    fun add(
        value: String, xDirAdj: Float, width: Float, yDirAdj: Float,
        heightDir: Float,
        height: Float
    ) {
        this.value += value
        if (xDirAdj < x || x == 0f) {
            x = xDirAdj
        }
        if (xDirAdj > toX) {
            toX = xDirAdj + width
        }
        if (y == 0f) {
            y = height - yDirAdj
        }
        this.height = heightDir
    }

    val width: Float
        get() = toX - x

    override fun toString(): String {
        return StringBuilder()
            .append("value: $value\t")
            .append("x: $x\t")
            .append("y: $y\t")
            .append("width: $width\t")
            .append("height: $height")
            .toString()
    }

    fun isEmpty() = x == 0.0f && y == 0.0f && width == 0.0f && height == 0.0f
}