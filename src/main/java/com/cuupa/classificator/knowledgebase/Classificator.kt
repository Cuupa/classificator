package com.cuupa.classificator.knowledgebase

import com.cuupa.classificator.knowledgebase.resultobjects.SemanticResult
import com.cuupa.classificator.knowledgebase.resultobjects.Topic
import com.cuupa.classificator.knowledgebase.stripper.PdfAnalyser
import com.cuupa.classificator.monitor.Monitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.juli.logging.LogFactory
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.ByteArrayInputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

class Classificator(private val manager: KnowledgeManager, private val analyser: PdfAnalyser,
                    private val monitor: Monitor) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun classify(text: String?): List<SemanticResult> {
        val start = LocalDateTime.now()
        val result = getResultFromInputText(text)
        val done = LocalDateTime.now()
        scope.launch {
            monitor.writeEvent(manager.getVersion(), text, result, start, done)
        }
        return result
    }

    private fun getResultFromInputText(text: String?): List<SemanticResult> {
        return if (text.isNullOrBlank()) {
            listOf(SemanticResult(Topic.OTHER, mutableListOf()))
        } else {
            manager.getResults(text)
        }
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
            log.error(e)
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
            log.error(e)
        }
        return pages
    }

    companion object {
        private val log = LogFactory.getLog(Classificator::class.java)
    }

}