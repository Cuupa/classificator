package com.cuupa.classificator.trainer.persistence.sqlite

import com.cuupa.classificator.trainer.service.Document
import java.util.*

class DocumentService(private val documentRepository: DocumentRepository) {

    fun list(): List<Document> {
        return try {
            documentRepository.findAll().map { mapToDomainObject(it) }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun save(document: Document) {
        documentRepository.save(mapToEntity(document))
    }

    fun getOpenDocuments(): List<Document> {
        return try {
            documentRepository.findAll().filter { !it.isDone }.map { mapToDomainObject(it) }
        } catch (e: Exception) {
            list()
        }
    }

    private fun mapToEntity(document: Document): DocumentEntity {
        return DocumentEntity().apply {
            content = document.content
            contentType = document.contentType
            plainText = document.plainText
            metadata = document.metadata.joinToString(separator = ";")
            results = document.results.joinToString(separator = ";")
            senders = document.senders.joinToString(separator = ";")
        }
    }

    private fun mapToDomainObject(it: DocumentEntity): Document {
        return Document(
            id = it.id,
            content = it.content,
            contentType = it.contentType,
            plainText = it.plainText
        )
    }

    fun find(id: String) = mapToDomainObject(documentRepository.findById(UUID.fromString(id)).orElse(DocumentEntity()))
}