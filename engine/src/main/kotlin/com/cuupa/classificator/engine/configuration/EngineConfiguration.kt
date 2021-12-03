package com.cuupa.classificator.engine.configuration

import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.ClassificatorImplementation
import com.cuupa.classificator.engine.ClassificatorOld
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.*
import com.cuupa.classificator.engine.services.kb.KnowledgeBase
import com.cuupa.classificator.engine.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.engine.stripper.PdfAnalyser
import com.cuupa.classificator.externalconfiguration.Config
import com.cuupa.classificator.externalconfiguration.configuration.ExternalConfiguration
import com.cuupa.classificator.monitor.service.Monitor
import org.apache.commons.logging.LogFactory
import org.apache.tika.Tika
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@Configuration
@Import(value = [ExternalConfiguration::class])
open class EngineConfiguration {

    @Autowired
    private var configuration: Config? = null

    @Value("\${classificator.kbfiles}")
    private var knowledgbaseDir: String = ""

    @Bean
    open fun classificator(
        knowledgeManager: KnowledgeManager,
        analyser: PdfAnalyser,
        monitor: Monitor,
        textExtractor: TextExtractor
    ): Classificator {
        return ClassificatorImplementation(knowledgeManager, analyser, monitor, textExtractor)
    }

    @Bean
    open fun textExtractor(tika: Tika): TextExtractor {
        return TextExtractor(tika)
    }

    @Bean
    open fun tika(): Tika {
        return Tika()
    }

    @Bean
    open fun classificatorOld(
        knowledgeManager: KnowledgeManager,
        analyser: PdfAnalyser,
        monitor: Monitor
    ): ClassificatorOld {
        return ClassificatorOld(knowledgeManager, analyser, monitor)
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
        metadataService: MetadataService
    ): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService(topicService, senderService, metadataService)
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
    open fun knowledgeBaseInitiator(): KnowledgeBaseInitiator {
        return KnowledgeBaseInitiator(getKnowledgeBaseDir())
    }

    @Bean
    open fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }

    private fun getKnowledgeBaseDir(): String {
        return knowledgbaseDir.ifEmpty {
            configuration?.classificator?.knowledgeBase ?: ""
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