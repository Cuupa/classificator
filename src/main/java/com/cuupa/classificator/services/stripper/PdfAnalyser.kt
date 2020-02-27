package com.cuupa.classificator.services.stripper

import com.cuupa.classificator.services.kb.SemanticResult
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.IOException
import java.util.*

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

    private fun getStripper(startPage: Int, endPage: Int): LocationAndSizeStripper {
        val stripper = LocationAndSizeStripper()
        stripper.startPage = startPage
        stripper.endPage = endPage
        return stripper
    }
}