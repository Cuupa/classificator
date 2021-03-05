package com.cuupa.classificator.knowledgebase.services.dataExtraction

import java.util.regex.Pattern

class PhoneNumberExtract(regex: String) : Extract(Pattern.compile(regex.trim()).toRegex()) {

    override fun normalize(value: String): String {
        return value
    }

    companion object{
        const val name = "[PHONENUMBER]"
    }
}