package com.cuupa.classificator.trainer.persistence.sqlite

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : JpaRepository<DocumentEntity, String> {

    fun findByDoneFalse(): List<DocumentEntity>

    @Query("SELECT DISTINCT d.batchName FROM DocumentEntity d")
    fun findDistinctBatchName(): List<String>

    fun findAllByBatchNameEquals(batchName: String?): List<DocumentEntity>

    fun deleteAllByBatchNameEquals(batchName: String?)

    @Query("SELECT DISTINCT d.expectedTopics FROM DocumentEntity d")
    fun findDistinctExpectedTopics(): List<String>

    @Query("SELECT DISTINCT d.actualTopics FROM DocumentEntity d")
    fun findDistinctActualTopics(): List<String>

    @Query("SELECT DISTINCT d.expectedSender FROM DocumentEntity d")
    fun findDistinctExpectedSender(): List<String>

    @Query("SELECT DISTINCT d.actualSender FROM DocumentEntity d")
    fun findDistinctActualSender(): List<String>

    @Query("SELECT DISTINCT d.expectedMetadata FROM DocumentEntity d")
    fun findDistinctExpectedMetadata(): List<String>

    @Query("SELECT DISTINCT d.actualMetadata FROM DocumentEntity d")
    fun findDistinctActualMetadata(): List<String>

}

