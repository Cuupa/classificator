package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

class IbanExtract(regex: String) : Extract(Pattern.compile(regex)) {

    override fun normalize(value: String): String {
        val charArray = value.replace(blank, empty).toCharArray()
        val sb = StringBuilder()
        for (i in charArray.indices) {
            if (i > 0 && i % 4 == 0) {
                sb.append(blank)
            }
            sb.append(charArray[i])
        }
        return sb.toString()
    }

    companion object {
        private const val blank = " "
        private const val empty = ""
    }
}