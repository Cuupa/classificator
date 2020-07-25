package com.cuupa.classificator.services

import com.cuupa.classificator.services.kb.KnowledgeManager
import com.cuupa.classificator.services.kb.SemanticResult
import com.cuupa.classificator.services.kb.semantic.Topic
import com.cuupa.classificator.services.stripper.PdfAnalyser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*

class Classificator(private val manager: KnowledgeManager, private val analyser: PdfAnalyser) {

    fun classify(text: String?): List<SemanticResult> {
        return if (text.isNullOrBlank()) {
            listOf(SemanticResult(Topic.OTHER, mutableListOf()))
        } else manager.getResults(text)
    }

    fun classify(content: ByteArray?): List<SemanticResult> {
        val results: MutableList<SemanticResult> = ArrayList()
        if (content == null || content.isEmpty()) {
            return results
        }
        try {
            PDDocument.load(ByteArrayInputStream(content)).use { document ->
                val text = java.lang.String.join("", extractText(document))
                results.addAll(manager.getResults(text))
                val resultFromStructure = analyser.getResults(document)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return results
    }

    private fun extractText(document: PDDocument): List<String> {
        val pages: MutableList<String> = ArrayList()
        try {
            for (page in 1..document.numberOfPages) {
                val stripper = PDFTextStripper()
                stripper.startPage = page
                stripper.endPage = page
                pages.add(stripper.getText(document))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return pages
    }

}