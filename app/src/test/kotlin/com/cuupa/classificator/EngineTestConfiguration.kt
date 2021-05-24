package com.cuupa.classificator

import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.*
import com.cuupa.classificator.engine.services.kb.KnowledgeBase
import com.cuupa.classificator.engine.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.engine.stripper.PdfAnalyser
import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.monitor.service.Monitor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [ExternalConfigurationTest::class])
open class EngineTestConfiguration {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.kbfiles}")
    private var knowledgbaseDir: String = ""

    @Bean
    open fun classificator(knowledgeManager: KnowledgeManager, analyser: PdfAnalyser, monitor: Monitor): Classificator {
        return Classificator(knowledgeManager, analyser, monitor)
    }

    @Bean
    open fun knowledgeManager(
        knowledgeBase: KnowledgeBase,
        executorService: KnowledgeBaseExecutorService
    ): KnowledgeManager {
        return KnowledgeManager(knowledgeBase, executorService)
    }

    @Bean
    open fun knowledgeBaseExecutorService(
        topicService: TopicService,
        senderService: SenderService,
        metadataService: MetadataService,
        languageDetectionService: LanguageDetectionService
    ): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService(topicService, senderService, metadataService, languageDetectionService)
    }

    @Bean
    open fun knowledgeBase() = knowledgeBaseInitiator().initKnowledgeBase()

    @Bean
    open fun topicService(knowledgeBase: KnowledgeBase): TopicService {
        return TopicService(knowledgeBase.topicList)
    }

    @Bean
    open fun senderService(knowledgeBase: KnowledgeBase): SenderService {
        return SenderService(knowledgeBase.sendersList)
    }

    @Bean
    open fun metadataService(knowledgeBase: KnowledgeBase): MetadataService {
        return MetadataService(knowledgeBase.metadataList)
    }

    @Bean
    open fun languageDetectionService(): LanguageDetectionService {
        return LanguageDetectionService()
    }

    @Bean
    open fun knowledgeBaseInitiator(): KnowledgeBaseInitiator {
        return KnowledgeBaseInitiator(getKnowledgeBaseDir())
    }

    @Bean
    open fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }

    private fun getKnowledgeBaseDir(): String {
        return if (knowledgbaseDir.isEmpty()) {
            configuration?.classificator?.knowledgeBase ?: ""
        } else {
            knowledgbaseDir
        }
    }
}
