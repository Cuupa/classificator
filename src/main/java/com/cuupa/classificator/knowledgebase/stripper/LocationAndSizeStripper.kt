package com.cuupa.classificator.knowledgebase.stripper

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition

class LocationAndSizeStripper : PDFTextStripper() {

    private val list = mutableListOf<TextAndPosition>()

    var height: Float = 0.0f

    override fun writeString(text: String, textPositions: List<TextPosition>) {
        parse(textPositions)
        writeString(text)
    }

    private fun parse(textPositions: List<TextPosition>) {
        var textAndPosition = TextAndPosition()
        for (textPosition in textPositions) {
            textAndPosition.add(
                textPosition.unicode, textPosition.xDirAdj,
                textPosition.widthDirAdj, textPosition.yDirAdj, textPosition.heightDir, height
            )
            if (isEmpty(textPosition)) {
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

    fun getTextAndPositions(document: PDDocument?): List<TextAndPosition> {
        super.getText(document)
        return list
    }
}