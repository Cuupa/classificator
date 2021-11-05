package com.cuupa.classificator.trainer.persistence

import com.cuupa.classificator.trainer.service.Document
import java.util.*

abstract class DocumentStorage {

    abstract fun write(document: List<Document>)
    abstract fun getOpenDocuments(): List<Document>
    abstract fun finishDocument(id: UUID, topics: List<String>)
    abstract fun find(id: String): Document
}
