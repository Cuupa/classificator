package com.cuupa.classificator.engine.extensions

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition

/**
 * @author Simon Thiel (https://github.com/cuupa) (24.05.2021)
 */
object Extension {

    fun PDDocument.getText(): List<String> {
        val pages = mutableListOf<String>()
        for (page in 1..numberOfPages) {
            val stripper = PDFTextStripper().apply {
                startPage = page
                endPage = page
            }
            pages.add(stripper.getText(this))
        }
        return pages
    }

    fun ByteArray?.isNullOrEmpty(): Boolean {
        return this == null || this.isEmpty()
    }

    fun TextPosition.isBlank(): Boolean {
        return unicode == null || unicode == " " || unicode == ""
    }

    fun String.substringBetween(leftDelimiter: String, rightDelimiter: String): String {
        return substringAfter(leftDelimiter).substringBefore(rightDelimiter)
    }

    fun Int.isPositive(): Boolean {
        return this >= 0
    }

    fun Long.isPositive(): Boolean {
        return this >= 0
    }
}