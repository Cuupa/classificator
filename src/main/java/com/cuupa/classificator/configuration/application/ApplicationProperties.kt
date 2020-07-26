package com.cuupa.classificator.configuration.application

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value

class ApplicationProperties {
    @Value("\${classificator.kbfiles}")
    val knowledgbaseDir: String = ""

    @Value("\${classificator.metadatafiles}")
    val metadataFiles: String = ""

    @Value("\${classificator.regexfiles}")
    val regexFiles: String = ""

    @Value("\${classificator.senderfiles}")
    val senderFiles: String = ""

    companion object {
        private val LOG = LogFactory.getLog(ApplicationProperties::class.java)
    }
}