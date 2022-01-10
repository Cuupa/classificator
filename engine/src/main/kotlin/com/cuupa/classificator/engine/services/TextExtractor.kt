package com.cuupa.classificator.engine.services

import com.cuupa.classificator.engine.extensions.Extension.getText
import com.cuupa.classificator.engine.extensions.Extension.isNullOrEmpty
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import org.springframework.http.MediaType
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

class TextExtractor(val tika: Tika) {

    fun extract(contentType: String?, content: ByteArray?): List<TextExtract> {
        return if ("application/zip" == contentType) {
            val zipEntries = getZipEntries(content)
            zipEntries.map { extractText(null, it) }
        } else {
            listOf(extractText(contentType, content))
        }
    }

    private fun getZipEntries(content: ByteArray?): MutableList<ByteArray> {
        val buffer = ByteArray(1024)
        val zipEntries = mutableListOf<ByteArray>()

        ZipInputStream(ByteArrayInputStream(content)).use { zis ->
            var zipEntry = zis.nextEntry
            while (zipEntry != null) {
                if (!zipEntry.isDirectory) {
                    val out = ByteArrayOutputStream()
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        out.write(buffer, 0, len)
                    }
                    zipEntries.add(out.toByteArray())
                }
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
        }
        return zipEntries
    }

    fun extractText(contentType: String?, content: ByteArray?): TextExtract {

        if (content.isNullOrEmpty()) {
            return TextExtract().apply {
                this.contentBytes = ByteArray(0)
                this.content = null
                this.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
            }
        }

        val byteContent = content!!

        when (contentType) {
            MediaType.APPLICATION_PDF_VALUE -> {
                PDDocument.load(ByteArrayInputStream(content)).use { document ->
                    return TextExtract().apply {
                        this.contentBytes = byteContent
                        this.content = document.getText().joinToString(separator = "")
                        this.contentType = contentType
                    }
                }
            }
            MediaType.TEXT_PLAIN_VALUE -> return TextExtract().apply {
                this.contentBytes = ByteArray(0)
                this.content = String(byteContent)
                this.contentType = contentType
            }

            else -> {
                return TextExtract().apply {
                    this.contentBytes = byteContent
                    this.content = tika.parseToString(ByteArrayInputStream(content))
                    this.contentType = tika.detector.detect(ByteArrayInputStream(content), Metadata()).toString()
                }
            }
        }
    }
}