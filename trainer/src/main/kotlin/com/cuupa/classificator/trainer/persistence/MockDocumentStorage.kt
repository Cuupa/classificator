package com.cuupa.classificator.trainer.persistence

import com.cuupa.classificator.trainer.services.Document

class MockDocumentStorage : DocumentStorage() {

    val storage = mutableListOf<Document>()

    override fun write(document: List<Document>) {
        document.forEach { storage.add(it) }
    }

    override fun getOpenDocuments(): List<Document> {
        TODO("Not yet implemented")
    }

    override fun find(id: String?): Document {
        TODO("Not yet implemented")
    }

    override fun getBatchNames(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getBatch(id: String?): List<Document> {
        TODO("Not yet implemented")
    }

    override fun complete(document: Document) {
        TODO("Not yet implemented")
    }

    override fun removeBatch(id: String?): Any {
        TODO("Not yet implemented")
    }
}
