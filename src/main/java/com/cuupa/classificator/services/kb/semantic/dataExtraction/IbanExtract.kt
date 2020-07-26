package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

class IbanExtract(regex: String) : Extract(Pattern.compile(regex)) {

    override fun normalize(value: String): String {
        val internValue = value.replace(" ", "")
        val sb = StringBuilder()
        val charArray = internValue.toCharArray()
        for (i in charArray.indices) {
            if (i % 4 == 0 && i > 0) {
                sb.append(" ")
            }
            sb.append(charArray[i])
        }
        return sb.toString()
    }
}