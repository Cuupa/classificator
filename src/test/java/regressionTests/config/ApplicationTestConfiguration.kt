package regressionTests.config

import com.cuupa.classificator.configuration.MonitorConfiguration
import com.cuupa.classificator.knowledgebase.Classificator
import com.cuupa.classificator.knowledgebase.KnowledgeManager
import com.cuupa.classificator.knowledgebase.TextExtractor
import com.cuupa.classificator.knowledgebase.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.knowledgebase.services.MetadataService
import com.cuupa.classificator.knowledgebase.services.SenderService
import com.cuupa.classificator.knowledgebase.services.TopicService
import com.cuupa.classificator.knowledgebase.services.kb.KnowledgeBase
import com.cuupa.classificator.knowledgebase.services.kb.KnowledgeBaseInitiator
import com.cuupa.classificator.knowledgebase.stripper.PdfAnalyser
import com.cuupa.classificator.monitor.Monitor
import org.apache.tika.Tika
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Configuration
@Import(value = [MonitorConfiguration::class])
open class ApplicationTestConfiguration {

    @Autowired
    private var monitor: Monitor? = null

    @Value("\${classificator.kbfiles}")
    var knowledgbaseDir: String = ""

    @Bean
    open fun classificator(): Classificator {
        return Classificator(knowledgeManager(), textExtractor(), monitor!!)
    }

    @Bean
    open fun textExtractor(): TextExtractor {
        return TextExtractor(tika())
    }

    @Bean
    open fun tika(): Tika {
        return Tika()
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
        return KnowledgeBaseInitiator(knowledgbaseDir)
    }

    @Bean
    open fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }
}