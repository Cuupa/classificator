package regressionTests.config

import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.*
import com.cuupa.classificator.engine.services.kb.KnowledgeBase
import com.cuupa.classificator.engine.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.engine.stripper.PdfAnalyser
import com.cuupa.classificator.monitor.service.Monitor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Configuration
@Import(value = [MonitorTestConfiguration::class])
open class EngineTestConfiguration {

    @Autowired
    private var monitor: Monitor? = null

    @Value("\${classificator.kbfiles}")
    var knowledgbaseDir: String = ""

    @Bean
    open fun classificator(): Classificator {
        return Classificator(knowledgeManager(), analyser(), monitor!!)
    }

    @Bean
    open fun knowledgeManager(): KnowledgeManager {
        return KnowledgeManager(knowledgeBase(), knowledgeBaseExecutorService())
    }

    @Bean
    open fun knowledgeBaseExecutorService(): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService(topicService(), senderService(), metadataService(), languageDetectionService())
    }

    @Bean
    open fun languageDetectionService(): LanguageDetectionService {
        return LanguageDetectionService()
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
        return KnowledgeBaseInitiator(knowledgbaseDir)
    }

    @Bean
    open fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }
}