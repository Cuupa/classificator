package com.cuupa.classificator.trainer.persistence.sqlite

import com.cuupa.classificator.trainer.persistence.DocumentStorage
import com.cuupa.classificator.trainer.service.Document
import java.util.*

internal class SqliteDocumentStorage(private val documentService: DocumentService) : DocumentStorage() {

    override fun write(document: List<Document>) = document.forEach { documentService.save(it) }
    override fun getOpenDocuments() = documentService.getOpenDocuments()

    override fun finishDocument(id: UUID, topics: List<String>) {
        TODO("Not yet implemented")
    }

    override fun find(id: String) = documentService.find(id)

}