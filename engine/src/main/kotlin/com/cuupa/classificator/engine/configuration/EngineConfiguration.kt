package com.cuupa.classificator.engine.configuration

import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.engine.services.MetadataService
import com.cuupa.classificator.engine.services.SenderService
import com.cuupa.classificator.engine.services.TopicService
import com.cuupa.classificator.engine.services.kb.KnowledgeBase
import com.cuupa.classificator.engine.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.engine.stripper.PdfAnalyser
import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.externalconfiguration.configuration.ExternalConfiguration
import com.cuupa.classificator.monitor.service.Monitor
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@Configuration
@Import(value = [ExternalConfiguration::class])
class EngineConfiguration {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.kbfiles}")
    private var knowledgbaseDir: String = ""

    @Bean
    fun classificator(knowledgeManager: KnowledgeManager, analyser: PdfAnalyser, monitor: Monitor): Classificator {
        return Classificator(knowledgeManager, analyser, monitor)
    }

    @Bean
    fun knowledgeManager(
        knowledgeBase: KnowledgeBase,
        executorService: KnowledgeBaseExecutorService
    ): KnowledgeManager {
        return KnowledgeManager(knowledgeBase, executorService)
    }

    @Bean
    fun knowledgeBaseExecutorService(
        topicService: TopicService,
        senderService: SenderService,
        metadataService: MetadataService
    ): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService(topicService, senderService, metadataService)
    }

    @Bean
    fun knowledgeBase() = knowledgeBaseInitiator().initKnowledgeBase()


    @Bean
    fun topicService(knowledgeBase: KnowledgeBase): TopicService {
        return TopicService(knowledgeBase.topicList)
    }

    @Bean
    fun senderService(knowledgeBase: KnowledgeBase): SenderService {
        return SenderService(knowledgeBase.sendersList)
    }

    @Bean
    fun metadataService(knowledgeBase: KnowledgeBase): MetadataService {
        return MetadataService(knowledgeBase.metadataList)
    }

    @Bean
    fun knowledgeBaseInitiator(): KnowledgeBaseInitiator {
        return KnowledgeBaseInitiator(getKnowledgeBaseDir())
    }

    @Bean
    fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }

    private fun getKnowledgeBaseDir(): String {
        return if (knowledgbaseDir.isEmpty()) {
            configuration?.classificator?.knowledgeBase ?: ""
        } else {
            knowledgbaseDir
        }
    }

    @PostConstruct
    fun configLoaded() {
        log.info("Loaded ${EngineConfiguration::class.simpleName}")
    }

    companion object {
        private val log = LogFactory.getLog(EngineConfiguration::class.java)
    }
}