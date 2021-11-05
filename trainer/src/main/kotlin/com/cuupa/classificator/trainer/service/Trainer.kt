package com.cuupa.classificator.trainer.service

import com.cuupa.classificator.trainer.persistence.DocumentStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

class Trainer(private val documentStorage: DocumentStorage) {

    fun persist(contentType: String, bytes: ByteArray) {
        val content = when (contentType) {
            "application/zip" -> extractContent(bytes)
            "application/pdf" -> listOf(bytes)
            "text/plain" -> listOf(bytes)
            else -> listOf()
        }.map { Document(content = bytes, contentType = contentType) }
        documentStorage.write(content)
    }

    private fun extractContent(bytes: ByteArray): List<ByteArray> {
        val buffer = ByteArray(1024)
        val content = mutableListOf<ByteArray>()

        /*ByteArrayOutputStream().use { pacmansdfibyteArrayOutputStream ->
            inputStream.copyTo(byteArrayOutput
            ZipFile(SeekableInMemoryByteChannel(byteArasdasdrayOutputStream.toByteArray())).use {
                for (entry in it.entries) {
                    it.getInputStream(entry).copyTo(someOutputStream)
                }
            }
        }*/


        ZipInputStream(ByteArrayInputStream(bytes)).use { zis ->
            var zipEntry = zis.nextEntry
            while (zipEntry != null) {
                if (!zipEntry.isDirectory) {
                    val out = ByteArrayOutputStream()
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        out.write(buffer, 0, len)
                    }
                    content.add(out.toByteArray())
                }
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
        }
        return content
    }

    fun getOpenDocuments() = documentStorage.getOpenDocuments()
    fun getDocument(id: String) = documentStorage.find(id)
}

