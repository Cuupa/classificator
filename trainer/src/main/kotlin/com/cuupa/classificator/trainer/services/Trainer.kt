package com.cuupa.classificator.trainer.services

import com.cuupa.classificator.trainer.persistence.DocumentStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

class Trainer(private val documentStorage: DocumentStorage) {

    fun persist(contentType: String, bytes: ByteArray, plainText: String?, batchName: String?, timestamp: Long) {

        val document = listOf(
            Document(
                content = bytes,
                contentType = contentType,
                plainText = plainText,
                batchName = batchName,
                timestamp = timestamp
            )
        )
        documentStorage.write(document)
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
    fun getDocument(id: String?) = documentStorage.find(id)
    fun getBatchNames() = documentStorage.getBatchNames()
    fun getBatch(id: String?) = documentStorage.getBatch(id)
    fun complete(document: Document) {
        documentStorage.complete(document)
    }
    fun removeBatch(id: String?) = documentStorage.removeBatch(id)
}

