package com.cuupa.classificator.knowledgebase.stripper

import org.apache.pdfbox.pdmodel.PDDocument
import java.io.IOException

class PdfAnalyser {

    fun getResults(document: PDDocument): List<List<TextAndPosition>> {
        val textAndPositions = mutableListOf<List<TextAndPosition>>()
        for (pageIndex in 1..document.numberOfPages) {
            try {
                val stripper = getStripper(pageIndex, pageIndex, document.getPage(pageIndex - 1).mediaBox.height)
                textAndPositions.add(stripper.getTextAndPositions(document))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return textAndPositions
    }

    private fun getStripper(startPage: Int, endPage: Int, height: Float): LocationAndSizeStripper {
        val stripper = LocationAndSizeStripper()
        stripper.startPage = startPage
        stripper.endPage = endPage
        stripper.height = height
        return stripper
    }
}