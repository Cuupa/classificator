package com.cuupa.classificator.trainer.services

import com.cuupa.classificator.trainer.persistence.DocumentStorage
import com.cuupa.classificator.trainer.persistence.sqlite.DocumentEntity
import com.cuupa.classificator.trainer.services.statistics.*

open class DocumentService(
    private val storage: DocumentStorage,
    private val precision: Precision,
    private val recall: Recall
) {

    fun list(): List<Document> = storage.findAll()?.map { it.mapToDomainObject() } ?: listOf()
    fun save(document: List<Document>) = storage.save(document.map { it.mapToEntity() })
    fun save(document: Document) = storage.save(document.mapToEntity())
    fun getOpenDocuments() = storage.getOpenDocuments().map { it.mapToDomainObject() }
    fun getBatchNames() = storage.getBatchNames().map { if (it.isEmpty()) "Unnamed" else it }
    fun find(id: String?) = storage.find(id).mapToDomainObject()
    fun getBatch(id: String?) = storage.getBatch(id).map { it.mapToDomainObject() }
    fun complete(document: Document) = storage.save(document.mapToEntity().apply { done = true })

    fun removeBatch(id: String?) = storage.removeBatch(id)

    fun getPrecision(name: String, type: MeasureType): PrecisionResult {
        return precision.getPrecision(storage.findAll()?.map { it.mapToDomainObject() } ?: listOf(), name, type)
    }

    fun getRecall(name: String, type: MeasureType): RecallResult {
        return recall.getRecall(storage.findAll()?.map { it.mapToDomainObject() } ?: listOf(), name, type)
    }

    fun getExpectedTopics() = storage.getDistinctExpectedTopics()
    fun getActualTopics() = storage.getDistinctActualTopics()
    fun getExpectedSender() = storage.getDistinctExpectedSender()
    fun getActualSender() = storage.getDistinctActualSender()
    fun getExpectedMetadata() = storage.getDistinctExpectedMetadata()
    fun getActualMetadata() = storage.getDistinctActualMetadata()
}

private fun DocumentEntity.mapToDomainObject(): Document {
    return Document(
        id = this.id,
        batchName = this.batchName,
        content = this.content,
        contentType = this.contentType,
        plainText = this.plainText,
        timestamp = this.timestamp,
        expectedTopics = this.expectedTopics.split(";").filter { !it.isNullOrEmpty() },
        expectedSenders = this.expectedSender.split(";").filter { !it.isNullOrEmpty() },
        expectedMetadata = this.expectedMetadata.split(";").filter { !it.isNullOrEmpty() },
        actualTopics = this.actualTopics.split(";").filter { !it.isNullOrEmpty() },
        actualSenders = this.actualSender.split(";").filter { !it.isNullOrEmpty() },
        actualMetadata = this.actualMetadata.split(";").filter { !it.isNullOrEmpty() }
    )
}

private fun Document.mapToEntity(): DocumentEntity {
    return DocumentEntity().apply {
        content = this@mapToEntity.content
        contentType = this@mapToEntity.contentType
        batchName = this@mapToEntity.batchName
        plainText = this@mapToEntity.plainText
        expectedMetadata = this@mapToEntity.expectedMetadata.joinToString(separator = ";")
        expectedTopics = this@mapToEntity.expectedTopics.joinToString(separator = ";")
        expectedSender = this@mapToEntity.expectedSenders.joinToString(separator = ";")
        actualTopics = this@mapToEntity.actualTopics.joinToString(separator = ";")
        actualSender = this@mapToEntity.actualSenders.joinToString(separator = ";")
        actualMetadata = this@mapToEntity.actualMetadata.joinToString(separator = ";")
        timestamp = this@mapToEntity.timestamp
    }
}