package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

class PhoneNumberExtract(regex: String) : Extract(Pattern.compile(regex)) {

    override fun normalize(value: String): String {
        return value
    }

    companion object{
        const val name = "[PHONENUMBER]"
    }
}