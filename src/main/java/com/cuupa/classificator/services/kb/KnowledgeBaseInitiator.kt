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
import java.nio.charset.StandardCharsets

class KnowledgeBaseInitiator(private val applicationProperties: ApplicationProperties) {
    private val log = LogFactory.getLog(KnowledgeBaseInitiator::class.java)

    fun initKnowledgeBase(): KnowledgeBase {
        val kb = KnowledgeBase()
        val knowledgebaseDir = ResourceUtils.getFile(applicationProperties.knowledgbaseDir)

        if (!knowledgebaseDir.isDirectory) {
            return kb
        }

        val files = knowledgebaseDir.listFiles() ?: return kb
        val listedFiles = files.first { it.name == "regex" }.listFiles() ?: return kb
        val regexContent = listedFiles.filter { it.name.endsWith(".regx") }.map { createRegex(it) }
        val metaDataTokenList = getMetaData(files)
        val topicList = files.filter { it.name.endsWith(".dsl") }.map { createTopic(it) }

        topicList.forEach { it.addMetaDataList(metaDataTokenList) }
        topicList.forEach { topic: Topic -> topic.metaDataList.forEach { it.setRegexContent(regexContent) } }
        kb.topicList = topicList.toMutableList()
        kb.senders = getSenders(applicationProperties.senderFiles).toMutableList()
        return kb
    }

    private fun getMetaData(files: Array<File>): List<MetaDataToken> {
        val metadataDir = files.first { it.name == "metadata" }
        val metadataFiles = metadataDir.listFiles() ?: return listOf()
        return metadataFiles.filter { it.name.endsWith(".meta") }.map { createMetaData(it) }
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
        return senderFiles.map { createSenderTokens(it) }.toMutableList()
    }
}