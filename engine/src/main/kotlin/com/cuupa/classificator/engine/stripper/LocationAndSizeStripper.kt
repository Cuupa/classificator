package com.cuupa.classificator.engine.stripper

import com.cuupa.classificator.engine.extensions.Extension.isBlank
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition

class LocationAndSizeStripper : PDFTextStripper() {
    @Volatile
    private var textAlreadyParsed = false
    private val list: MutableList<TextAndPosition> = mutableListOf()

    override fun writeString(text: String, textPositions: List<TextPosition>) {
        if (!textAlreadyParsed) {
            parse(textPositions)
            textAlreadyParsed = true
        }
        writeString(text)
    }

    private fun parse(textPositions: List<TextPosition>) {
        var textAndPosition = TextAndPosition()
        for (textPosition in textPositions) {
            if (!textPosition.isBlank()) {
                textAndPosition.add(
                    textPosition.unicode, textPosition.xDirAdj,
                    textPosition.widthDirAdj, textPosition.yDirAdj, textPosition.heightDir
                )
            } else {
                textAndPosition.add(
                    textPosition.unicode, textPosition.xDirAdj,
                    textPosition.widthDirAdj, textPosition.yDirAdj, textPosition.heightDir
                )
                list.add(textAndPosition)
                textAndPosition = TextAndPosition()
            }
        }
        if (!list.contains(textAndPosition)) {
            list.add(textAndPosition)
        }
    }

    fun setTextAlreadyParsed() {
        textAlreadyParsed = true
    }

    fun getTextAndPositions(document: PDDocument?): List<TextAndPosition> {
        super.getText(document)
        return list
    }
}