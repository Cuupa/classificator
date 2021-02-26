package com.cuupa.classificator.knowledgebase.services.dataExtraction

import com.cuupa.classificator.constants.StringConstants
import org.apache.logging.log4j.util.Strings
import java.util.regex.Pattern

class RegexExtract(regex: String) : Extract(Pattern.compile(regex.trim(), Pattern.CASE_INSENSITIVE)) {

    override fun normalize(value: String): String {
        return value.replace(StringConstants.blank, Strings.EMPTY).replace("\n", "").replace("\r", "")
    }
}