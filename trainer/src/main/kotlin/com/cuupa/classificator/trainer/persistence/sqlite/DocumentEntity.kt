package com.cuupa.classificator.trainer.persistence.sqlite

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class DocumentEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
    )
    @Column(name = "id", updatable = false, nullable = false)
    var id: String? = null

    var batchName: String? = null

    var content: ByteArray? = null

    var contentType: String? = null

    var plainText: String? = null

    lateinit var topics: String

    lateinit var senders: String

    lateinit var metadata: String

    var done: Boolean = false

    var timestamp: Long = -1L
}