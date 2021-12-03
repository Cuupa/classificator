package com.cuupa.classificator.engine

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.engine.extensions.Extension.getText
import com.cuupa.classificator.engine.extensions.Extension.isNullOrEmpty
import com.cuupa.classificator.engine.stripper.PdfAnalyser
import com.cuupa.classificator.monitor.service.Monitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.logging.LogFactory
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.ByteArrayInputStream
import java.io.IOException
import java.time.LocalDateTime

class ClassificatorOld(
    private val manager: KnowledgeManager, private val analyser: PdfAnalyser,
    private val monitor: Monitor
) : Classificator {

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun classify(text: String?): List<SemanticResult> {
        val start = LocalDateTime.now()
        val result = manager.getResults(text)
        val done = LocalDateTime.now()
        scope.launch {
            monitor.writeEvent(manager.getVersion(), text, result, start, done)
        }
        return result
    }

    override fun classify(contentType: String?, content: String?): Pair<String, List<SemanticResult>> {
        TODO("Not yet implemented")
    }

    fun classify(content: ByteArray?): List<SemanticResult> {
        val results: MutableList<SemanticResult> = ArrayList()
        if (content.isNullOrEmpty()) {
            return results
        }
        try {
            PDDocument.load(ByteArrayInputStream(content)).use { document ->
                val text = document.getText().joinToString(separator = "")
                results.addAll(manager.getResults(text))
                val resultFromStructure = analyser.getResults(document)
            }
        } catch (e: IOException) {
            log.error(e)
        }
        return results
    }

    companion object {
        private val log = LogFactory.getLog(Classificator::class.java)
    }
}

