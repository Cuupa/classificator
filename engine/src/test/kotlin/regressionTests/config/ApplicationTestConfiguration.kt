package regressionTests.config

import com.cuupa.classificator.engine.Classificator
import com.cuupa.classificator.engine.KnowledgeManager
import com.cuupa.classificator.engine.services.KnowledgeBaseExecutorService
import com.cuupa.classificator.engine.services.MetadataService
import com.cuupa.classificator.engine.services.SenderService
import com.cuupa.classificator.engine.services.TopicService
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
class ApplicationTestConfiguration {

    @Autowired
    private var monitor: Monitor? = null

    @Value("\${classificator.kbfiles}")
    var knowledgbaseDir: String = ""

    @Bean
    fun classificator(): Classificator {
        return Classificator(knowledgeManager(), analyser(), monitor!!)
    }

    @Bean
    fun knowledgeManager(): KnowledgeManager {
        return KnowledgeManager(knowledgeBase(), knowledgeBaseExecutorService())
    }

    @Bean
    fun knowledgeBaseExecutorService(): KnowledgeBaseExecutorService {
        return KnowledgeBaseExecutorService(topicService(), senderService(), metadataService())
    }

    @Bean
    fun knowledgeBase(): KnowledgeBase {
        return knowledgeBaseInitiator().initKnowledgeBase()
    }

    @Bean
    fun topicService(): TopicService {
        return TopicService(knowledgeBase().topicList)
    }

    @Bean
    fun senderService(): SenderService {
        return SenderService(knowledgeBase().sendersList)
    }

    @Bean
    fun metadataService(): MetadataService {
        return MetadataService(knowledgeBase().metadataList)
    }

    @Bean
    fun knowledgeBaseInitiator(): KnowledgeBaseInitiator {
        return KnowledgeBaseInitiator(knowledgbaseDir)
    }

    @Bean
    fun analyser(): PdfAnalyser {
        return PdfAnalyser()
    }
}