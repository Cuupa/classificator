package regressionTests.config

import com.cuupa.classificator.configuration.application.ApplicationProperties
import com.cuupa.classificator.monitor.EventStorage
import com.cuupa.classificator.monitor.FileEventStorage
import com.cuupa.classificator.monitor.Monitor
import com.cuupa.classificator.services.Classificator
import com.cuupa.classificator.services.kb.*
import com.cuupa.classificator.services.stripper.PdfAnalyser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Configuration
open class TestConfig {

    @Bean
    open fun classificator(): Classificator {
        return Classificator(knowledgeManager(), analyser())
    }

    @Bean
    open fun knowledgeManager(): KnowledgeManager {
        return KnowledgeManager(knowledgeBase(), knowledgeBaseExecutorService())
    }

    @Bean
    open fun knowledgeBaseExecutorService(): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService(topicService(), senderService(), metadataService())
    }

    @Bean
    open fun knowledgeBase(): KnowledgeBase {
        return knowledgeBaseInitiator().initKnowledgeBase()
    }

    @Bean
    open fun topicService(): TopicService {
        return TopicService(knowledgeBase().topicList)
    }

    @Bean
    open fun senderService(): SenderService {
        return SenderService(knowledgeBase().sendersList)
    }

    @Bean
    open fun metadataService(): MetadataService {
        return MetadataService(knowledgeBase().metadataList)
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
    open fun monitor(): Monitor {
        return Monitor(eventStorage())
    }

    @Bean
    open fun eventStorage(): EventStorage {
        return FileEventStorage()
    }

    @Bean
    open fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }
}