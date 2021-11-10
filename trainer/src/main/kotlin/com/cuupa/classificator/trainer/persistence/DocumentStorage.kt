package com.cuupa.classificator.trainer.persistence

import com.cuupa.classificator.trainer.services.Document

abstract class DocumentStorage {

    abstract fun write(document: List<Document>)
    abstract fun getOpenDocuments(): List<Document>
    abstract fun find(id: String?): Document
    abstract fun getBatchNames(): List<String>
    abstract fun getBatch(id: String?): List<Document>
    abstract fun complete(document: Document)
    abstract fun removeBatch(id: String?): Any
}
