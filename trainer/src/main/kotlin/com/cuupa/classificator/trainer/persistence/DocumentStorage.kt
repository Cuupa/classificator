package com.cuupa.classificator.trainer.persistence

import com.cuupa.classificator.trainer.persistence.sqlite.DocumentEntity

abstract class DocumentStorage {

    abstract fun save(document: List<DocumentEntity>)
    abstract fun save(document: DocumentEntity)
    abstract fun getOpenDocuments(): List<DocumentEntity>
    abstract fun find(id: String?): DocumentEntity
    abstract fun findAll(): List<DocumentEntity>?
    abstract fun getBatchNames(): List<String>
    abstract fun getBatch(id: String?): List<DocumentEntity>
    abstract fun complete(document: DocumentEntity)
    abstract fun removeBatch(id: String?): Any
    abstract fun getDistinctExpectedTopics(): List<String>
    abstract fun getDistinctActualTopics(): List<String>
    abstract fun getDistinctExpectedSender(): List<String>
    abstract fun getDistinctActualSender(): List<String>
    abstract fun getDistinctExpectedMetadata(): List<String>
    abstract fun getDistinctActualMetadata(): List<String>
    abstract fun count(): Long
}
