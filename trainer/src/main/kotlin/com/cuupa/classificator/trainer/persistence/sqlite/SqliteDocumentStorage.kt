package com.cuupa.classificator.trainer.persistence.sqlite

import com.cuupa.classificator.trainer.persistence.DocumentStorage
import com.cuupa.classificator.trainer.services.Document

internal class SqliteDocumentStorage(private val documentService: DocumentService) : DocumentStorage() {

    override fun write(document: List<Document>) = document.forEach { documentService.save(it) }
    override fun getOpenDocuments() = documentService.getOpenDocuments()

    override fun find(id: String?) = documentService.find(id)
    override fun getBatchNames() = documentService.getBatchNames()
    override fun getBatch(id: String?) = documentService.getBatch(id)
    override fun complete(document: Document) {
        documentService.complete(document)
    }

    override fun removeBatch(id: String?) = documentService.removeBatch(id)
}