package com.cuupa.classificator.engine.services

import com.cuupa.classificator.engine.extensions.Extension.getText
import com.cuupa.classificator.engine.extensions.Extension.isNullOrEmpty
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import org.springframework.http.MediaType
import java.io.ByteArrayInputStream

class TextExtractor(val tika: Tika) {

    fun extractText(contentType: String?, content: ByteArray?): TextExtract {

        if (content.isNullOrEmpty()) {
            return TextExtract().apply {
                this.content = null
                this.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
            }
        }

        when (contentType) {
            MediaType.APPLICATION_PDF_VALUE -> {
                PDDocument.load(ByteArrayInputStream(content)).use { document ->
                    return TextExtract().apply {
                        this.content = document.getText().joinToString(separator = "")
                        this.contentType = contentType
                    }
                }
            }
            MediaType.TEXT_PLAIN_VALUE -> return TextExtract().apply {
                this.content = String(content!!)
                this.contentType = contentType
            }
            else -> {
                return TextExtract().apply {
                    this.content = tika.parseToString(ByteArrayInputStream(content))
                    this.contentType = tika.detector.detect(ByteArrayInputStream(content), Metadata()).toString()
                }
            }
        }
    }
}