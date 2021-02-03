package com.cuupa.classificator.services.kb.services.knowledgebase

import com.cuupa.classificator.configuration.application.ApplicationProperties
import org.apache.commons.logging.LogFactory
import java.io.File

class KnowledgeBaseInitiator(private val applicationProperties: ApplicationProperties) {

    private val knowledgebaseEnding = ".db"

    private val versionRegex = "([\\d]+[.]?)+[^.db]".toRegex()

    fun initKnowledgeBase(): KnowledgeBase {
        val knowledgebase = File(applicationProperties.knowledgbaseDir)

        val kb = if (isFileSpecified(knowledgebase)) {
            KnowledgeFileExtractor.extractKnowledgebase(knowledgebase)
        } else if (knowledgebase.isDirectory) {
            val files = knowledgebase.list()?.filter { it.endsWith(knowledgebaseEnding) } ?: listOf()
            val maxVersion = getMaxVersion(files)
            val filename = getFilename(maxVersion, files)

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
            log.info("Successfully loaded Knowledgbase $knowledgebase")
            log.info("Running Knowledgebase ${kb.knowledgeBaseMetadata.version}")
        }
        return kb
    }

    private fun isFileSpecified(knowledgebase: File) =
        knowledgebase.isFile && knowledgebase.name.endsWith(knowledgebaseEnding)

    private fun getFilename(maxVersion: Int?, files: List<String>) =
        if (maxVersion != null) {
            val regex = "[$maxVersion.]+.db".toRegex()
            files.firstOrNull {
                val result = regex.find(it)
                if (result == null) {
                    false
                } else {
                    it.endsWith(result.value)
                }
            }
        } else {
            files.firstOrNull { it.endsWith(".db") }
        }

    private fun getMaxVersion(files: List<String>) =
        files.mapNotNull { versionRegex.find(it) }.map { it.value.replace(".", "") }.map { it.toInt() }
            .maxOfOrNull { it }

    companion object {
        private val log = LogFactory.getLog(KnowledgeBaseInitiator::class.java)
    }
}