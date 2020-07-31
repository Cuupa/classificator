package com.cuupa.classificator.services.kb.semantic.dataExtraction

import org.apache.logging.log4j.util.Strings
import java.util.regex.Pattern

class RegexExtract(regex: String) : Extract(Pattern.compile(regex)) {

    override fun normalize(value: String): String {
        return value.replace(blank, Strings.EMPTY)
    }

    companion object {
        private const val blank = " "
    }
}