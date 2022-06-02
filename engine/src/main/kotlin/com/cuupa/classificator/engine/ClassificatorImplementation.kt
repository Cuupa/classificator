package com.cuupa.classificator.engine

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.engine.extensions.Extension.getText
import com.cuupa.classificator.engine.extensions.Extension.isNullOrEmpty
import com.cuupa.classificator.engine.services.TextExtractor
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
import java.util.*

class ClassificatorImplementation(
    private val manager: KnowledgeManager, private val analyser: PdfAnalyser,
    private val monitor: Monitor,
    private val textExtractor: TextExtractor
) : Classificator {
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun classify(content: String?): List<SemanticResult> {
        return classify("text/plain", content).second
    }

    override fun classify(contentType: String?, content: String?): Pair<String, List<SemanticResult>> {
        val start = LocalDateTime.now()
        val result =
            try {
                val isBase64 = content?.let { it.endsWith("=").and(base64Regex.matches(it)) } ?: false
                val contentBytes =
                    content?.let { if (isBase64) decodeBase64(it) else it.encodeToByteArray() } ?: ByteArray(0)

                val extractText = textExtractor.extractText(content = contentBytes, contentType = contentType)
                Pair(extractText.contentType, manager.getResults(extractText.content))
            } catch (e: Exception) {
                log.error("Invalid content-type or content")
                Pair("application/octet-stream", listOf())
            }

        val done = LocalDateTime.now()
        scope.launch {
            monitor.writeEvent(
                manager.getVersion(),
                result.second.firstOrNull()?.originalText,
                result.second,
                start,
                done
            )
        }
        return result
    }

    private fun isSupportedContentType(contentType: String) = supportedContentTypes.contains(contentType)

    @Deprecated(message = "")
    fun classifyOld(contentBytes: ByteArray?): List<SemanticResult> {
        if (contentBytes.isNullOrEmpty()) {
            return listOf()
        }
        return try {
            PDDocument.load(ByteArrayInputStream(contentBytes)).use { document ->
                val text = document.getText().joinToString(separator = "")
                return manager.getResults(text)
                //val resultFromStructure = analyser.getResults(document)
            }
        } catch (e: IOException) {
            log.error(e)
            listOf()
        }
    }

    @Deprecated(message = "")
    fun classifyOld(text: String?): List<SemanticResult> {
        val start = LocalDateTime.now()
        val result = manager.getResults(text)
        val done = LocalDateTime.now()
        scope.launch {
            monitor.writeEvent(manager.getVersion(), text, result, start, done)
        }
        return result
    }

    private fun decodeBase64(content: String?) = Base64.getDecoder().decode(content)

    companion object {
        private val log = LogFactory.getLog(ClassificatorImplementation::class.java)
        private val base64Regex = Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")
        private val supportedContentTypes = listOf("text/plain", "application/pdf")
    }
}

