package com.cuupa.classificator.engine.services.kb

import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.services.kb.KnowledgeFileExtractor.Constants.dslSuffix
import com.cuupa.classificator.engine.services.kb.KnowledgeFileExtractor.Constants.metaSuffix
import com.cuupa.classificator.engine.services.kb.KnowledgeFileExtractor.Constants.regxSuffix
import com.cuupa.classificator.engine.services.kb.KnowledgeFileExtractor.Constants.senderSuffix
import com.cuupa.classificator.engine.services.kb.KnowledgeFileParser.parseDatabaseMetadata
import com.cuupa.classificator.engine.services.kb.KnowledgeFileParser.parseMetaFile
import com.cuupa.classificator.engine.services.kb.KnowledgeFileParser.parseRegexFile
import com.cuupa.classificator.engine.services.kb.KnowledgeFileParser.parseSenderFile
import com.cuupa.classificator.engine.services.kb.KnowledgeFileParser.parseTopicFile
import com.cuupa.classificator.engine.services.token.MetaDataToken
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import java.io.File
import java.nio.charset.StandardCharsets

object KnowledgeFileExtractor {

    fun extractKnowledgebase(knowledgebase: File): KnowledgeFile {
        return KnowledgeFile().apply {
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
                    entry?.name?.let { filename ->
                        when {
                            isTopicFile(filename) -> topicList.add(parseTopicFile(getString(entry, sevenZFile)))
                            isSenderFile(filename) -> senderList.add(parseSenderFile(getString(entry, sevenZFile)))
                            isMetadataFile(filename) -> metadataList.add(parseMetaFile(getString(entry, sevenZFile)))
                            isRegexFile(filename) -> regexList.add(parseRegexFile(getRegexName(filename), getString(entry, sevenZFile)))
                            isDatabaseMetaInfo(filename) -> kbMetadata = parseDatabaseMetadata(getString(entry, sevenZFile))
                            else -> { }
                        }
                    }
                }
                topics = topicList
                metadata = metadataList
                senders= senderList
                regex = regexList
            }
        }
    }

    private fun isToParse(entry: SevenZArchiveEntry?) =
        entry == null || entry.isDirectory

    private fun getRegexName(filename: String) = filename.substringAfter("regex/")

    private fun isDatabaseMetaInfo(filename: String?) = filename == "META.INF"

    private fun isRegexFile(filename: String) = filename.endsWith(regxSuffix)

    private fun isMetadataFile(filename: String) = filename.endsWith(metaSuffix)

    private fun isSenderFile(filename: String) = filename.endsWith(senderSuffix)

    private fun isTopicFile(filename: String) = filename.endsWith(dslSuffix)


    private fun getString(
        entry: SevenZArchiveEntry?,
        sevenZFile: SevenZFile
    ): String {
        if (entry == null) {
            return ""
        }
        val byteArray = ByteArray(entry.size.toInt()).also { sevenZFile.read(it, 0, it.size) }
        return String(byteArray, StandardCharsets.UTF_8)
    }

    internal object Constants {
        const val metaSuffix = ".meta"
        const val dslSuffix = ".dsl"
        const val regxSuffix = ".regx"
        const val senderSuffix = ".sender"
    }
}
