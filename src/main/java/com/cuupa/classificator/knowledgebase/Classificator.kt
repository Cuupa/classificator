package com.cuupa.classificator.knowledgebase

import com.cuupa.classificator.knowledgebase.resultobjects.SemanticResult
import com.cuupa.classificator.knowledgebase.resultobjects.Topic
import com.cuupa.classificator.knowledgebase.stripper.PdfAnalyser
import com.cuupa.classificator.monitor.Monitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.juli.logging.LogFactory
import org.apache.tika.Tika
import java.time.LocalDateTime

class Classificator(
    private val manager: KnowledgeManager,
    private val extractor: TextExtractor,
    private val monitor: Monitor
) {

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
        if (content == null || content.isEmpty()) {
            return listOf()
        }
        val start = LocalDateTime.now()
        val extract = extractor.extract(content)
        val result = getResultFromInputText(extract.content)
        val done = LocalDateTime.now()
        scope.launch {
            monitor.writeEvent(manager.getVersion(), extract.content, result, start, done)
        }
        return result
    }


    companion object {
        private val log = LogFactory.getLog(Classificator::class.java)
    }

}