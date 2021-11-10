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
}

