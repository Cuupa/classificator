package com.cuupa.classificator.engine.stripper

import com.cuupa.classificator.domain.SemanticResult
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.IOException

class PdfAnalyser {

    fun getResults(document: PDDocument): List<SemanticResult>? {
        val textAndPositions: MutableList<List<TextAndPosition>> = ArrayList()
        for (pageIndex in 1..document.numberOfPages) {
            try {
                val stripper = getStripper(pageIndex, pageIndex)
                textAndPositions.add(stripper.getTextAndPositions(document))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun getStripper(startPage: Int, endPage: Int) = LocationAndSizeStripper().apply {
        this.startPage = startPage
        this.endPage = endPage
    }
}