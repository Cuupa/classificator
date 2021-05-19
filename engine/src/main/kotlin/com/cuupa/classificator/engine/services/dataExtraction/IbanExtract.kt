package com.cuupa.classificator.engine.services.dataExtraction

import com.cuupa.classificator.engine.StringConstants
import org.apache.logging.log4j.util.Strings

class IbanExtract(regex: String) : Extract(Regex(regex.trim(), RegexOption.IGNORE_CASE)) {

    override fun normalize(value: String): String {
        val charArray = value.replace(StringConstants.blank, Strings.EMPTY).toCharArray()
        val sb = StringBuilder()
        for (i in charArray.indices) {
            if (i > 0 && i % 4 == 0) {
                sb.append(" ")
            }
            sb.append(charArray[i])
        }
        return sb.toString()
    }

    companion object {
        const val name = "[IBAN]"
    }
}