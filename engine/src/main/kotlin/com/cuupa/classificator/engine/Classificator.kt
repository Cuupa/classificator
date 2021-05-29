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

class Classificator(
    private val manager: KnowledgeManager, private val analyser: PdfAnalyser,
    private val monitor: Monitor
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun classify(contentType: String?, content: String?): Pair<String, List<SemanticResult>> {
        val start = LocalDateTime.now()

        val isBase64 = content?.let { base64Regex.matches(it) } ?: false
        val finalContent = if (isBase64) getContent(content) else content

        var result: Pair<String, List<SemanticResult>> = Pair("application/octet-stream", listOf())
        try {
            result = classify(contentType, finalContent)
        } catch (e: Exception) {
            log.error("Invalid content-type or content")
            val detectedContentType = detectContentType(finalContent)
            when {
                isSupportedContentType(detectedContentType) -> result = classify(detectedContentType, finalContent)
                else -> log.error("Content-Type $detectedContentType is not supported")
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

    private fun classify(
        contentType: String?,
        finalContent: Serializable?,
    ): Pair<String, List<SemanticResult>> {
        return when (contentType) {
            "application/pdf" -> Pair(contentType, classifyPdf(finalContent as ByteArray))
            "text/plain" -> Pair(contentType, classifyText(finalContent as String))
            else -> classifyOther(finalContent as ByteArray)
        }
    }

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
    fun classify(content: ByteArray?): List<SemanticResult> {
        return classifyPdf(content)
    }

    @Deprecated(message = "")
    fun classify(text: String?): List<SemanticResult> {
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
        private val log = LogFactory.getLog(Classificator::class.java)
        private val tika = Tika()

        val base64Regex = Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")

        private val supportedContentTypes = listOf("text/plain", "application/pdf")
    }
}

