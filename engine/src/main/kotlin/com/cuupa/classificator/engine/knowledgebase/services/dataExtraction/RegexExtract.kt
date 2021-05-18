package com.cuupa.classificator.engine.knowledgebase.services.dataExtraction

import com.cuupa.classificator.engine.StringConstants
import org.apache.logging.log4j.util.Strings

class RegexExtract(regex: String) : Extract(Regex(regex.trim(), RegexOption.IGNORE_CASE)) {

    override fun normalize(value: String): String {
        return value.replace(StringConstants.blank, Strings.EMPTY).replace("\n", "").replace("\r", "")
    }
}