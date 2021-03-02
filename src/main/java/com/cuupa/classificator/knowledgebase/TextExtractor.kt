package com.cuupa.classificator.knowledgebase

import org.apache.juli.logging.LogFactory
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.tika.Tika
import java.io.ByteArrayInputStream
import java.io.IOException

class TextExtractor(private val tika: Tika) {

    private fun isPdf(mimetype: String) = "application/pdf" == mimetype

    fun extract(content: ByteArray): ExtractResult {
        if(content.isEmpty()){
            return ExtractResult("", "")
        }
        val byteArrayInputStream = ByteArrayInputStream(content)
        val mimetype = tika.detect(byteArrayInputStream)
        return if (isPdf(mimetype)) {
            PDDocument.load(byteArrayInputStream).use {
                ExtractResult(extractText(it).joinToString("", "", ""), mimetype)
            }
        } else{
            ExtractResult(tika.parseToString(byteArrayInputStream), mimetype)
        }
    }

    private fun extractText(document: PDDocument): List<String> {
        val pages = mutableListOf<String>()
        try {
            for (page in 1..document.numberOfPages) {
                val stripper = PDFTextStripper()
                stripper.startPage = page
                stripper.endPage = page
                pages.add(stripper.getText(document))
            }
        } catch (e: IOException) {
            log.error(e)
        }
        return pages
    }

    companion object{
        private val log = LogFactory.getLog(TextExtractor::class.java)
    }
}
