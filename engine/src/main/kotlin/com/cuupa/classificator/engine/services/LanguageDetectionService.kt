package com.cuupa.classificator.engine.services

import org.apache.commons.logging.LogFactory
import org.apache.tika.language.detect.LanguageDetector

class LanguageDetectionService {

    private var languageDetector: LanguageDetector? = null

    init {
        languageDetector =
            try {
                LanguageDetector.getDefaultLanguageDetector()
            } catch (e: IllegalStateException) {
                log.error(e)
                null
            }
    }

    fun getLanguages(text: String): List<String> {
        return languageDetector?.let { detector ->
            detector.detectAll(text)
                .filter { it.isReasonablyCertain }
                .map { it.language }
        }.orEmpty()
    }

    companion object {
        private val log = LogFactory.getLog(LanguageDetectionService::class.java)
    }
}
