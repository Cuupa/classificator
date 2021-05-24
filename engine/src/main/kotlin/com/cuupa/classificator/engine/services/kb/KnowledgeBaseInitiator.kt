package com.cuupa.classificator.engine.services.kb

import org.apache.commons.logging.LogFactory
import java.io.File

class KnowledgeBaseInitiator(private val knowledgebaseDir: String) {

    private val knowledgebaseEnding = ".db"

    private val versionRegex = "([\\d]+[.]?)+[^.db]".toRegex()

    fun initKnowledgeBase(): KnowledgeBase {
        val knowledgebase = File(knowledgebaseDir)
        val kb = getKnowledgebase(knowledgebase)

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

    private fun getKnowledgebase(knowledgebase: File): KnowledgeBase {
        return when {
            isFileSpecified(knowledgebase) -> KnowledgeFileExtractor.extractKnowledgebase(knowledgebase).create()
            knowledgebase.isDirectory -> {
                val files = knowledgebase.list()?.filter { it.endsWith(knowledgebaseEnding) } ?: listOf()
                val filename = getFilename(getMaxVersion(files), files)

                when {
                    !filename.isNullOrEmpty() -> KnowledgeFileExtractor.extractKnowledgebase(File(knowledgebase, filename)).create()
                    else -> KnowledgeBase()
                }
            }
            else -> KnowledgeBase()
        }
    }

    private fun isFileSpecified(knowledgebase: File) =
        knowledgebase.isFile && knowledgebase.name.endsWith(knowledgebaseEnding)

    private fun getFilename(maxVersion: Int?, files: List<String>): String? {
        maxVersion?.let {
            return files.findLast { matches("[$maxVersion.]+.db".toRegex(), it) }
        } ?: return files.findLast { it.endsWith(".db") }
    }

    private fun matches(regex: Regex, string: String): Boolean {
        return regex.find(string)?.let { string.endsWith(it.value) } ?: false
    }

    private fun getMaxVersion(files: List<String>) =
        files.mapNotNull { versionRegex.find(it) }
            .map { it.value.replace(".", "") }
            .map { it.tryToInt() }
            .maxOfOrNull { it }

    companion object {
        private val log = LogFactory.getLog(KnowledgeBaseInitiator::class.java)
    }
}

private fun String.tryToInt(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}
