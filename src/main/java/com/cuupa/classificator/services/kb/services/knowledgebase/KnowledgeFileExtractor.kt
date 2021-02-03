package com.cuupa.classificator.services.kb.services.knowledgebase

import com.cuupa.classificator.services.kb.Sender
import com.cuupa.classificator.services.kb.Topic
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken
import com.cuupa.classificator.services.kb.services.knowledgebase.KnowledgeFileParser.parseDatabaseMetadata
import com.cuupa.classificator.services.kb.services.knowledgebase.KnowledgeFileParser.parseMetaFile
import com.cuupa.classificator.services.kb.services.knowledgebase.KnowledgeFileParser.parseRegexFile
import com.cuupa.classificator.services.kb.services.knowledgebase.KnowledgeFileParser.parseSenderFile
import com.cuupa.classificator.services.kb.services.knowledgebase.KnowledgeFileParser.parseTopic
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import java.io.File
import java.nio.charset.StandardCharsets

object KnowledgeFileExtractor {

    fun extractKnowledgebase(knowledgebase: File): KnowledgeBase {
        val kb = KnowledgeBase()
        SevenZFile(knowledgebase).use { sevenZFile ->
            val topicList = mutableListOf<Topic>()
            val senderList = mutableListOf<Sender>()
            val metadataList = mutableListOf<MetaDataToken>()
            val regexList = mutableListOf<Pair<String, String>>()
            var entry: SevenZArchiveEntry?
            while (sevenZFile.nextEntry.also { entry = it } != null) {
                if (isToParse(entry)) {
                    continue
                }
                val filename = entry!!.name
                when {
                    isTopicFile(filename) -> topicList.add(parseTopic(readIntoString(entry, sevenZFile)))
                    isSenderFile(filename) -> senderList.add(parseSenderFile(readIntoString(entry, sevenZFile)))
                    isMetadataFile(filename) -> metadataList.add(parseMetaFile(readIntoString(entry, sevenZFile)))
                    isRegexFile(filename) -> regexList.add(
                        parseRegexFile(
                            getRegexName(filename),
                            readIntoString(entry, sevenZFile)
                        )
                    )
                    isDatabaseMetaInfo(filename) -> kb.knowledgeBaseMetadata =
                        parseDatabaseMetadata(readIntoString(entry, sevenZFile))
                }
            }
            kb.topicList = topicList
            kb.metadataList = metadataList
            kb.sendersList = senderList
            kb.metadataList.forEach { it.setRegexContent(regexList) }
        }
        return kb
    }

    private fun isToParse(entry: SevenZArchiveEntry?) =
        entry == null || entry.isDirectory

    private fun getRegexName(filename: String) = filename.substringAfter("regex/")

    private fun isDatabaseMetaInfo(filename: String?) = filename == "META.INF"

    private fun isRegexFile(filename: String) = filename.endsWith(".regx")

    private fun isMetadataFile(filename: String) = filename.endsWith(".meta")

    private fun isSenderFile(filename: String) = filename.endsWith(".sender")

    private fun isTopicFile(filename: String) = filename.endsWith(".dsl")


    private fun readIntoString(
        entry: SevenZArchiveEntry?,
        sevenZFile: SevenZFile
    ): String {
        if (entry == null) {
            return ""
        }
        val byteArray = ByteArray(entry.size.toInt())
        sevenZFile.read(byteArray, 0, byteArray.size)
        return String(byteArray, StandardCharsets.UTF_8)
    }
}