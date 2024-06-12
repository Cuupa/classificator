package com.cuupa.classificator.engine.services.kb

import org.apache.commons.logging.LogFactory
import java.io.File

class KnowledgeBaseInitiator(private val knowledgebaseDir: String) {

    private val knowledgebaseEnding = ".db"

    private val versionRegex = "([\\d]+[.]?)+[^.db]".toRegex()

    fun initKnowledgeBase(): KnowledgeBase {
        val file = File(knowledgebaseDir)
        val kb = getKnowledgebase(file)

        if (!kb.isValid()) {
            log.error("No knowledgebase found for ${file.absolutePath}")
        } else {
            log.error("Successfully loaded Knowledgbase ${file.absolutePath}")
            log.error("Running Knowledgebase ${kb.knowledgeBaseMetadata.version}")
            log.error("Loaded ${kb.topicList.size} topic definitions")
            log.error("Loaded ${kb.sendersList.size} sender definitions")
            log.error("Loaded ${kb.metadataList.size} metadata definitions")
        }
        return kb
    }

    private fun getKnowledgebase(file: File): KnowledgeBase {
        return when {
            isFileSpecified(file) -> KnowledgeFileExtractor.extractKnowledgebase(file).create()
            file.isDirectory -> {
                val files = file.list()?.filter { it.endsWith(knowledgebaseEnding) } ?: listOf()
                val filename = getFilename(getMaxVersion(files), files)
                when {
                    !filename.isNullOrEmpty() -> KnowledgeFileExtractor.extractKnowledgebase(File(file, filename))
                        .create()
                    else -> KnowledgeBase()
                }
            }
            else -> KnowledgeBase()
        }
    }

    private fun isFileSpecified(file: File) = file.isFile && file.name.endsWith(knowledgebaseEnding)

    private fun getFilename(maxVersion: String?, files: List<String>): String? {
        maxVersion?.let {
            return files.findLast { matches("$maxVersion\\.db".toRegex(), it) }
        } ?: return files.findLast { it.endsWith(".db") }
    }

    private fun matches(regex: Regex, string: String): Boolean {
        return regex.find(string)?.let {
            string.endsWith(it.value)
        } ?: false
    }

    private fun getMaxVersion(files: List<String>) = files.mapNotNull { versionRegex.find(it) }.maxOfOrNull { it.value }

    companion object {
        private val log = LogFactory.getLog(KnowledgeBaseInitiator::class.java)
    }
}


