package com.cuupa.classificator.services.kb

import com.cuupa.classificator.configuration.application.ApplicationProperties
import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.tuple.Pair
import org.apache.commons.logging.LogFactory
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.util.function.Consumer

class KnowledgeBaseInitiator(private val applicationProperties: ApplicationProperties) {
    private val log = LogFactory.getLog(KnowledgeBaseInitiator::class.java)

    fun initKnowledgeBase(): KnowledgeBase {
        val kb = KnowledgeBase()
        try {
            val knowledgebaseDir = ResourceUtils.getFile(applicationProperties.knowledgbaseDir)

            if (!knowledgebaseDir.isDirectory) {
                return kb
            }

            val files = knowledgebaseDir.listFiles() ?: return kb
            val regexList = files.first { e: File -> e.name == "regex" }
            val listedFiles = regexList.listFiles() ?: return kb

            val regexContent: List<Pair<String, String>> = listedFiles.filter { e: File ->
                e.name.endsWith(".regx")
            }.map { regexFile: File -> createRegex(regexFile) }

            val metaDataTokenList = getMetaData(files)

            val topicList: List<Topic> = files
                    .filter { e: File ->
                        e.name.endsWith(".dsl")
                    }
                    .map { kbFile: File -> createTopic(kbFile) }

            topicList.forEach(Consumer { topic: Topic -> topic.addMetaDataList(metaDataTokenList) })
            topicList.forEach(Consumer { topic: Topic ->
                topic.metaDataList
                        .forEach(Consumer { token: MetaDataToken -> token.setRegexContent(regexContent) })
            })
            kb.topicList = topicList.toMutableList()
            kb.senders = getSenders(applicationProperties.senderFiles!!).toMutableList()
        } catch (fnfe: FileNotFoundException) {
            log.error("Error loading files", fnfe)
        }
        return kb
    }

    private fun getMetaData(files: Array<File>): List<MetaDataToken> {
        val metadataDir = files.first { e: File -> e.name == "metadata" }
        val metadataFiles = metadataDir.listFiles() ?: return emptyList()
        return metadataFiles.filter { e: File ->
            e.name.endsWith(".meta")
        }.map { metaFile: File -> createMetaData(metaFile) }
    }

    private fun createMetaData(metaFile: File): MetaDataToken {
        return KnowledgeFileParser.parseMetaFile(FileUtils.readFileToString(metaFile, StandardCharsets.UTF_8))
    }

    private fun createRegex(regexFile: File): Pair<String, String> {
        return KnowledgeFileParser.parseRegexFile(regexFile.name,
                FileUtils.readFileToString(regexFile, StandardCharsets.UTF_8))
    }

    private fun createTopic(kbFile: File): Topic {
        return KnowledgeFileParser.parseTopicFile(FileUtils.readFileToString(kbFile, StandardCharsets.UTF_8))
    }

    private fun createSenderTokens(file: File): SenderToken {
        return KnowledgeFileParser.parseSenderFile(FileUtils.readFileToString(file, StandardCharsets.UTF_8))
    }

    private fun getSenders(senderFolderString: String): MutableList<SenderToken> {
        val senderFolder = ResourceUtils.getFile(senderFolderString)
        val senderFiles = senderFolder.listFiles() ?: return mutableListOf()
        return senderFiles
                .map { file: File -> createSenderTokens(file) }.toMutableList()
    }
}