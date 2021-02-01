package com.cuupa.classificator.services.kb.services.knowledgebase

import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import java.io.File
import java.nio.charset.StandardCharsets

object KnowledgeFileExtractor {

    fun extractKnowledgebase(knowledgebase: File): KnowledgeBase {
        val kb = KnowledgeBase()
        SevenZFile(knowledgebase).use { sevenZFile ->
            val topicList = mutableListOf<Topic>()
            val senderList = mutableListOf<SenderToken>()
            val metadataList = mutableListOf<MetaDataToken>()
            val regexList = mutableListOf<Pair<String, String>>()
            var entry: SevenZArchiveEntry?
            while (sevenZFile.nextEntry.also { entry = it } != null) {
                if (entry == null || entry!!.isDirectory) {
                    continue
                }
                when {
                    entry!!.name.endsWith(".dsl") ->
                        topicList.add(KnowledgeFileParser.parseTopic(readIntoString(entry, sevenZFile)))
                    entry!!.name.endsWith("sender") ->
                        senderList.add(KnowledgeFileParser.parseSenderFile(readIntoString(entry, sevenZFile)))
                    entry!!.name.endsWith("meta") ->
                        metadataList.add(KnowledgeFileParser.parseMetaFile(readIntoString(entry, sevenZFile)))
                    entry!!.name.endsWith("regx") ->
                        regexList.add(
                            KnowledgeFileParser.parseRegexFile(
                                entry!!.name.substringAfter("regex/"),
                                readIntoString(entry, sevenZFile)
                            )
                        )
                    entry!!.name == "META.INF" -> {
                        val metaInfContent = readIntoString(entry!!, sevenZFile)
                        kb.version = metaInfContent.substringAfter("version=").substringBefore("\n")
                    }
                }
            }
            kb.topicList = topicList
            kb.metadataList = metadataList
            kb.sendersList = senderList
            kb.metadataList.forEach { it.setRegexContent(regexList) }
        }
        return kb
    }


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