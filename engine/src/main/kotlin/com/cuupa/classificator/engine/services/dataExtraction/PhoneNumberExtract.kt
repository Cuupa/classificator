package com.cuupa.classificator.engine.services.dataExtraction

class PhoneNumberExtract(regex: String) : Extract(Regex(regex.trim())) {

    override fun normalize(value: String) = value

    companion object {
        const val name = "[PHONENUMBER]"
    }
}