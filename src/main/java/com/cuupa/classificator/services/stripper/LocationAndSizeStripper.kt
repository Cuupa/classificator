package com.cuupa.classificator.services.stripper

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
            if (!isEmpty(textPosition)) {
                textAndPosition.add(textPosition.unicode, textPosition.xDirAdj,
                        textPosition.widthDirAdj, textPosition.yDirAdj, textPosition.heightDir)
            } else {
                textAndPosition.add(textPosition.unicode, textPosition.xDirAdj,
                        textPosition.widthDirAdj, textPosition.yDirAdj, textPosition.heightDir)
                list.add(textAndPosition)
                textAndPosition = TextAndPosition()
            }
        }
        if (!list.contains(textAndPosition)) {
            list.add(textAndPosition)
        }
    }

    private fun isEmpty(textPosition: TextPosition): Boolean {
        return textPosition.unicode == null || textPosition.unicode == " "
    }

    fun setTextAlreadyParsed() {
        textAlreadyParsed = true
    }

    fun getTextAndPositions(document: PDDocument?): List<TextAndPosition> {
        super.getText(document)
        return list
    }
}