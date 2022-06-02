package com.cuupa.classificator.trainer.persistence.sqlite

import com.cuupa.classificator.trainer.persistence.DocumentStorage
import org.springframework.transaction.annotation.Transactional

open class SqliteDocumentStorage(private val documentRepository: DocumentRepository) : DocumentStorage() {

    @Transactional(value = "trainer_transactionManager")
    override fun save(document: List<DocumentEntity>) = document.forEach { documentRepository.save(it) }

    @Transactional(value = "trainer_transactionManager")
    override fun save(document: DocumentEntity) {
        documentRepository.save(document)
    }

    override fun getOpenDocuments() = documentRepository.findByDoneFalse()

    override fun find(id: String?) = documentRepository.findById(id).orElse(DocumentEntity())
    override fun findAll() = documentRepository.findAll()
    override fun findAllCompleted() = documentRepository.findAllByDoneTrue()
    override fun getBatchNames() = documentRepository.findDistinctBatchName()
    override fun getBatch(id: String?) = documentRepository.findAllByBatchNameEquals(id)

    @Transactional(value = "trainer_transactionManager")
    override fun complete(document: DocumentEntity) {
        documentRepository.save(document.apply { done = true })
    }

    @Transactional(value = "trainer_transactionManager")
    override fun removeBatch(id: String?) = documentRepository.deleteAllByBatchNameEquals(id)
    override fun getDistinctExpectedTopics() = documentRepository.findDistinctExpectedTopics()
    override fun getDistinctActualTopics() = documentRepository.findDistinctActualTopics()
    override fun getDistinctExpectedSender() = documentRepository.findDistinctExpectedSender()
    override fun getDistinctActualSender() = documentRepository.findDistinctActualSender()
    override fun getDistinctExpectedMetadata() = documentRepository.findDistinctExpectedMetadata()
    override fun getDistinctActualMetadata() = documentRepository.findDistinctActualMetadata()
    override fun count() = documentRepository.count()
}