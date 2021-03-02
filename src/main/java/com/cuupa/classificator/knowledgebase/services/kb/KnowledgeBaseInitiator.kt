package com.cuupa.classificator.knowledgebase.services.kb

import org.apache.commons.logging.LogFactory
import java.io.File

class KnowledgeBaseInitiator(private val knowledgebaseDir: String) {

    private val knowledgebaseEnding = ".db"

    private val versionRegex = "([\\d]+[.]?)+[^.db]".toRegex()

    fun initKnowledgeBase(): KnowledgeBase {
        val knowledgebase = File(knowledgebaseDir)

        val kb = if (isFileSpecified(knowledgebase)) {
            KnowledgeFileExtractor.extractKnowledgebase(knowledgebase)
        } else if (knowledgebase.isDirectory) {
            val files = knowledgebase.list()?.filter { it.endsWith(knowledgebaseEnding) } ?: listOf()
            val filename = getFilename(getMaxVersion(files), files)

            if (filename.isNullOrEmpty()) {
                KnowledgeBase()
            } else {
                KnowledgeFileExtractor.extractKnowledgebase(File(knowledgebase, filename))
            }
        } else {
            KnowledgeBase()
        }

        if (!kb.isValid()) {
            log.error("No knowledgebase found for $knowledgebase")
        } else {
            log.error("Successfully loaded Knowledgbase $knowledgebase")
            log.error("Running Knowledgebase ${kb.knowledgeBaseMetadata.version}")
            log.error("Loaded ${kb.topicList.size} topic definitions")
            log.error("Loaded ${kb.sendersList.size} sender definitions")
            log.error("Loaded ${kb.metadataList.size} metadata definitions")
        }
        return kb
    }

    private fun isFileSpecified(knowledgebase: File) =
        knowledgebase.isFile && knowledgebase.name.endsWith(knowledgebaseEnding)

    private fun getFilename(maxVersion: Int?, files: List<String>) =
        if (maxVersion != null) {
            val regex = "[$maxVersion.]+.db".toRegex()
            files.findLast { matches(regex, it) }
        } else {
            files.findLast { it.endsWith(".db") }
        }

    private fun matches(regex: Regex, it: String): Boolean {
        val result = regex.find(it)
        return if (result == null) {
            false
        } else {
            it.endsWith(result.value)
        }
    }

    private fun getMaxVersion(files: List<String>) =
        files.mapNotNull { versionRegex.find(it) }.map { it.value.replace(".", "") }.map {
            try {
                it.toInt()
            } catch(e: Exception){
                0
            }
        }
            .maxOfOrNull { it }

    companion object {
        private val log = LogFactory.getLog(KnowledgeBaseInitiator::class.java)
    }
}