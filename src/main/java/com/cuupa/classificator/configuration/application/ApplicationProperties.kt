package com.cuupa.classificator.configuration.application

import org.springframework.beans.factory.annotation.Value

class ApplicationProperties {

    @Value("\${classificator.kbfiles}")
    var knowledgbaseDir: String = ""
}