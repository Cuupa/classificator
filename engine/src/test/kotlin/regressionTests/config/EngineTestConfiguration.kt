package regressionTests.config

import com.cuupa.classificator.engine.ClassificatorImplementation
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.*
import com.cuupa.classificator.engine.services.kb.KnowledgeBase
import com.cuupa.classificator.engine.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.engine.stripper.PdfAnalyser
import com.cuupa.classificator.monitor.service.Monitor
import org.apache.tika.Tika
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

    @Value("\${classificator.kbfiles}")
    var knowledgbaseDir: String = ""

    @Bean
    open fun classificator(
        knowledgeManager: KnowledgeManager,
        analyser: PdfAnalyser,
        monitor: Monitor,
        textExtractor: TextExtractor
    ): ClassificatorImplementation {
        return ClassificatorImplementation(knowledgeManager, analyser, monitor, textExtractor)
    }

    @Bean
    open fun tika(): Tika {
        return Tika()
    }

    @Bean
    open fun textExtractor(tika: Tika): TextExtractor {
        return TextExtractor(tika)
    }

    @Bean
    open fun knowledgeManager(knowledgeBase: KnowledgeBase, knowledgeBaseExecutorService: KnowledgeBaseExecutorService): KnowledgeManager {
        return KnowledgeManager(knowledgeBase, knowledgeBaseExecutorService)
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