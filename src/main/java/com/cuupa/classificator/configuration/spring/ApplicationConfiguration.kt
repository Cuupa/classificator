package com.cuupa.classificator.configuration.spring

import com.cuupa.classificator.configuration.application.ApplicationProperties
import com.cuupa.classificator.monitor.EventStorage
import com.cuupa.classificator.monitor.FileEventStorage
import com.cuupa.classificator.monitor.Monitor
import com.cuupa.classificator.services.Classificator
import com.cuupa.classificator.services.kb.KnowledgeBaseExecutorService
import com.cuupa.classificator.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.services.kb.KnowledgeManager
import com.cuupa.classificator.services.stripper.PdfAnalyser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ApplicationConfiguration {

    @Bean
    open fun classificator(): Classificator {
        return Classificator(knowledgeManager(), analyser(), monitor())
    }

    @Bean
    open fun monitor(): Monitor {
        return Monitor(eventStorage())
    }

    @Bean
    open fun eventStorage(): EventStorage {
        return FileEventStorage()
    }

    @Bean
    open fun knowledgeManager(): KnowledgeManager {
        return KnowledgeManager(knowledgeBaseInitiator(), knowledgeBaseExecutorService())
    }

    @Bean
    open fun knowledgeBaseExecutorService(): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService()
    }

    @Bean
    open fun knowledgeBaseInitiator(): KnowledgeBaseInitiator {
        return KnowledgeBaseInitiator(applicationProperties())
    }

    @Bean
    open fun applicationProperties(): ApplicationProperties {
        return ApplicationProperties()
    }

    @Bean
    open fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }
}