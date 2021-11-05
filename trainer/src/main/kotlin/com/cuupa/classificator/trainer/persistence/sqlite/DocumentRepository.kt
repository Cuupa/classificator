package com.cuupa.classificator.trainer.persistence.sqlite

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DocumentRepository : JpaRepository<DocumentEntity, UUID>