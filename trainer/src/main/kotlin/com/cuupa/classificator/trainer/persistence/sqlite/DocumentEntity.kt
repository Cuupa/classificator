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
    var id: String = ""

    var batchName: String = ""

    var content: ByteArray = ByteArray(0)

    var contentType: String = ""

    var plainText: String = ""

    var expectedTopics: String = ""

    var expectedSender: String = ""

    var expectedMetadata: String = ""

    var actualTopics: String = ""

    var actualSender: String = ""

    var actualMetadata: String = ""

    var done: Boolean = false

    var timestamp: Long = -1L
}