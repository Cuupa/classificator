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
import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

class ClassificatorImplementation(
    private val manager: KnowledgeManager, private val analyser: PdfAnalyser,
    private val monitor: Monitor
) : Classificator {
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun classify(content: String?): List<SemanticResult> {
        return classify("text/plain", content).second
    }

    override fun classify(contentType: String?, content: String?): Pair<String, List<SemanticResult>> {
        val start = LocalDateTime.now()

        val isBase64 = content?.let { it.endsWith("=").and(base64Regex.matches(it)) } ?: false
        val contentString: String? = null
        val contentBytes: ByteArray? = if (isBase64) getContent(content) else null

        val result = try {
            when (contentType) {
                "application/pdf" -> Pair(contentType, classifyPdf(getContent(content) as ByteArray))
                "text/plain" -> Pair(contentType, classifyText(content as String))
                else -> classifyOther(getContent(content))
            }
        } catch (e: Exception) {
            log.error("Invalid content-type or content")
            val detectedContentType = detectContentType(content)
            when {
                isSupportedContentType(detectedContentType) -> classifyOther(getContent(content))
                else -> {
                    log.error("Content-Type $detectedContentType is not supported")
                    Pair("application/octet-stream", listOf())
                }
            }
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

    private fun classifyOther(content: ByteArray): Pair<String, List<SemanticResult>> {
        val text = tika.parseToString(ByteArrayInputStream(content))
        val contentType = tika.detector.detect(ByteArrayInputStream(content), Metadata()).toString()
        val result = classifyText(text)
        return Pair(contentType, result)
    }

    private fun detectContentType(content: Serializable?): String {
        return when (content) {
            is String -> return "plain/text"
            is ByteArray -> return tika.detector.detect(ByteArrayInputStream(content), Metadata()).toString()
            else -> "application/octet-stream"
        }
    }

    private fun classifyPdf(contentBytes: ByteArray?): List<SemanticResult> {
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

    private fun classifyText(text: String?) = manager.getResults(text)

    @Deprecated(message = "")
    fun classifyOld(content: ByteArray?): List<SemanticResult> {
        return classifyPdf(content)
    }

    @Deprecated(message = "")
    fun classifyOld(text: String?): List<SemanticResult> {
        val start = LocalDateTime.now()
        val result = classifyText(text)
        val done = LocalDateTime.now()
        scope.launch {
            monitor.writeEvent(manager.getVersion(), text, result, start, done)
        }
        return result
    }

    private fun getContent(content: String?) = Base64.getDecoder().decode(content)

    companion object {
        private val log = LogFactory.getLog(ClassificatorImplementation::class.java)
        private val tika = Tika()

        val base64Regex = Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")

        private val supportedContentTypes = listOf("text/plain", "application/pdf")
    }
}

