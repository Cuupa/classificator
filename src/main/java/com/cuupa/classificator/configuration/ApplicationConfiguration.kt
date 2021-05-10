package com.cuupa.classificator.configuration

import com.cuupa.classificator.configuration.external.Config
import com.cuupa.classificator.knowledgebase.Classificator
import com.cuupa.classificator.knowledgebase.KnowledgeManager
import com.cuupa.classificator.knowledgebase.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.knowledgebase.services.MetadataService
import com.cuupa.classificator.knowledgebase.services.SenderService
import com.cuupa.classificator.knowledgebase.services.TopicService
import com.cuupa.classificator.knowledgebase.services.kb.KnowledgeBase
import com.cuupa.classificator.knowledgebase.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.knowledgebase.stripper.PdfAnalyser
import com.cuupa.classificator.monitor.Monitor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [MonitorConfiguration::class, ExternalConfiguration::class, SecurityConfiguration::class])
open class ApplicationConfiguration {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.kbfiles}")
    private var knowledgbaseDir: String = ""

    @Bean
    open fun classificator(knowledgeManager: KnowledgeManager, analyser: PdfAnalyser, monitor: Monitor): Classificator {
        return Classificator(knowledgeManager, analyser, monitor)
    }

    @Bean
    open fun knowledgeManager(knowledgeBase: KnowledgeBase, executorService: KnowledgeBaseExecutorService): KnowledgeManager {
        return KnowledgeManager(knowledgeBase, executorService)
    }

    @Bean
    open fun knowledgeBaseExecutorService(topicService: TopicService, senderService: SenderService, metadataService: MetadataService): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService(topicService, senderService, metadataService)
    }

    @Bean
    open fun knowledgeBase(): KnowledgeBase {
        return knowledgeBaseInitiator().initKnowledgeBase()
    }

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