package regressionTests.config

import com.cuupa.classificator.configuration.application.ApplicationProperties
import com.cuupa.classificator.services.kb.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

open class LocalRegressionTest {

    var applicationProperties = ApplicationProperties()

    var knowledgeManager: KnowledgeManager

    fun getFilesOfPath(path: String): List<File> {
        return Files.list(Paths.get(path)).map { it.toFile() }.collect(Collectors.toList()) ?: listOf()
    }

    fun getFileContent(file: File): String {
        return PDDocument.load(file).use { getTextPerPage(it) }.joinToString(" ", "", "")
    }

    private fun getTextPerPage(document: PDDocument): List<String> {
        val pages: MutableList<String> = ArrayList(document.numberOfPages)
        for (page in 0..document.numberOfPages) {
            val stripper = PDFTextStripper()
            stripper.startPage = page
            stripper.endPage = page
            pages.add(stripper.getText(document))
        }
        return pages
    }

    init {
        applicationProperties.knowledgbaseDir =
            "/home/${System.getProperty("user.name")}/IntelliJProjects/classificator/src/main/resources/kbfiles"
        val knowledgeBase = KnowledgeBaseInitiator(applicationProperties).initKnowledgeBase()
        val topicService = TopicService(knowledgeBase.topicList)
        val senderService = SenderService(knowledgeBase.sendersList)
        val metadataService = MetadataService(knowledgeBase.metadataList)
        knowledgeManager =
            KnowledgeManager(knowledgeBase, KnowledgeBaseExecutorService(topicService, senderService, metadataService))
    }
}
