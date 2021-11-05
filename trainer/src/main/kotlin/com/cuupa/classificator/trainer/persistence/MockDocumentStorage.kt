package com.cuupa.classificator.trainer.persistence

import com.cuupa.classificator.trainer.service.Document
import java.util.*

class MockDocumentStorage : DocumentStorage() {

    val storage = mutableListOf<Document>()

    override fun write(document: List<Document>) {
        document.forEach { storage.add(it) }
    }

    override fun getOpenDocuments(): List<Document> {
        TODO("Not yet implemented")
    }

    override fun finishDocument(id: UUID, topics: List<String>) {
        TODO("Not yet implemented")
    }

    override fun find(id: String): Document {
        TODO("Not yet implemented")
    }
}
