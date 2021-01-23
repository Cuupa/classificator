package com.cuupa.classificator.configuration.application

import org.springframework.beans.factory.annotation.Value

class ApplicationProperties {

    @Value("\${classificator.kbfiles}")
    var knowledgbaseDir: String = ""

    @Value("\${classificator.metadatafiles}")
    var metadataFiles: String = ""

    @Value("\${classificator.regexfiles}")
    var regexFiles: String = ""

    @Value("\${classificator.senderfiles}")
    var senderFiles: String = ""
}