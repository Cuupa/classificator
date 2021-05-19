package com.cuupa.classificator.monitor.persistence.sqlite

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class EventEntity {

    lateinit var kbVersion: String

    var text: String? = null

    lateinit var results: String

    lateinit var senders:String

    lateinit var metadata :String

    @Id
    var start: LocalDateTime = LocalDateTime.MIN

    var end: LocalDateTime = LocalDateTime.MIN
}