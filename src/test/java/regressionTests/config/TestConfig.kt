package regressionTests.config

import com.cuupa.classificator.configuration.application.ApplicationProperties
import com.cuupa.classificator.services.Classificator
import com.cuupa.classificator.services.kb.KnowledgeBaseExecutorService
import com.cuupa.classificator.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.services.kb.KnowledgeManager
import com.cuupa.classificator.services.stripper.PdfAnalyser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Configuration
open class TestConfig {

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
    open fun classificator(): Classificator {
        return Classificator(knowledgeManager(), analyser())
    }

    @Bean
    open fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }
}