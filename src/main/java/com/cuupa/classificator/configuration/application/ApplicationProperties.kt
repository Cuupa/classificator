package com.cuupa.classificator.configuration.application

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value

class ApplicationProperties {
    @Value("\${classificator.kbfiles}")
    val knowledgbaseDir: String? = null
    @Value("\${classificator.metadatafiles}")
    val metadataFiles: String? = null
    @Value("\${classificator.regexfiles}")
    val regexFiles: String? = null
    @Value("\${classificator.senderfiles}")
    val senderFiles: String? = null

    companion object {
        private val LOG = LogFactory.getLog(ApplicationProperties::class.java)
    }
}