package com.cuupa.classificator.knowledgebase

import org.apache.juli.logging.LogFactory
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.tika.Tika
import java.io.ByteArrayInputStream
import java.io.IOException

class TextExtractor(private val tika: Tika) {

    private val eof = byteArrayOf(37, 37, 69, 79, 70)

    fun extract(content: ByteArray): ExtractResult {
        if (content.isEmpty()) {
            return ExtractResult("", "")
        }
        val byteArrayInputStream = ByteArrayInputStream(content)
        return if (isPdf(content)) {
            PDDocument.load(byteArrayInputStream).use {
                ExtractResult(extractText(it).joinToString("", "", ""), "application/pdf")
            }
        } else {
            val mimetype = tika.detect(byteArrayInputStream)
            ExtractResult(tika.parseToString(byteArrayInputStream), mimetype)
        }
    }

    private fun isPdf(content: ByteArray): Boolean {
        // index of the first "%" of %%EOF
        var indexContent = content.size - 6
        var indexEOF = 0
        var eofFound = true
        // TODO: refactor
        while(indexContent != content.size-1) {
            if(content[indexContent++] != eof[indexEOF++]){
                eofFound = false
            }
        }
        return eofFound
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
