package com.cuupa.classificator.knowledgebase.services.dataExtraction

class PhoneNumberExtract(regex: String) : Extract(Regex(regex.trim())) {

    override fun normalize(value: String): String {
        return value
    }

    companion object{
        const val name = "[PHONENUMBER]"
    }
}