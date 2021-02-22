package com.cuupa.classificator.services.kb.semantic.dataExtraction

import com.cuupa.classificator.constants.StringConstants
import org.apache.logging.log4j.util.Strings
import java.util.regex.Pattern

class IbanExtract(regex: String) : Extract(Pattern.compile(regex.trim(), Pattern.CASE_INSENSITIVE)) {

    override fun normalize(value: String): String {
        val charArray = value.replace(StringConstants.blank, Strings.EMPTY).toCharArray()
        val sb = StringBuilder()
        for (i in charArray.indices) {
            if (i > 0 && i % 4 == 0) {
                sb.append(Strings.EMPTY)
            }
            sb.append(charArray[i])
        }
        return sb.toString()
    }

    companion object {
        const val name = "[IBAN]"
    }
}