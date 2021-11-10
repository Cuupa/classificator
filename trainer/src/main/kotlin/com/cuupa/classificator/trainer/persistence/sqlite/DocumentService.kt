package com.cuupa.classificator.trainer.persistence.sqlite

import com.cuupa.classificator.trainer.services.Document
import javax.transaction.Transactional

open class DocumentService(private val documentRepository: DocumentRepository) {

    fun list(): List<Document> = documentRepository.findAll().map { it.mapToDomainObject() }
    fun save(document: Document) = documentRepository.save(document.mapToEntity())
    fun getOpenDocuments() = documentRepository.findByDoneFalse().map { it.mapToDomainObject() }
    fun getBatchNames() = documentRepository.findDistinctBatchName().map { if (it.isNullOrEmpty()) "Unnamed" else it }
    fun find(id: String?) = documentRepository.findById(id).orElse(DocumentEntity()).mapToDomainObject()
    fun getBatch(id: String?) = documentRepository.findAllByBatchNameEquals(id)?.map { it.mapToDomainObject() }
    fun complete(document: Document) = documentRepository.save(document.mapToEntity().apply { done = true })

    @Transactional
    open fun removeBatch(id: String?) = documentRepository.deleteAllByBatchNameEquals(id)
}

private fun DocumentEntity.mapToDomainObject(): Document {
    return Document(
        id = this.id,
        batchName = this.batchName,
        content = this.content,
        contentType = this.contentType,
        plainText = this.plainText,
        timestamp = timestamp
    )
}

private fun Document.mapToEntity(): DocumentEntity {
    return DocumentEntity().apply {
        content = this@mapToEntity.content
        contentType = this@mapToEntity.contentType
        batchName = this@mapToEntity.batchName
        plainText = this@mapToEntity.plainText
        metadata = this@mapToEntity.metadata.joinToString(separator = ";")
        topics = this@mapToEntity.topics.joinToString(separator = ";")
        senders = this@mapToEntity.senders.joinToString(separator = ";")
        timestamp = this@mapToEntity.timestamp
    }
}