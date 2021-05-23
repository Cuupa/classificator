package com.cuupa.classificator.engine.services.dataExtraction

import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PhoneNumberTest : ExtractTest() {

    private val unitToTest = PhoneNumberExtract(knowledgeFile.regex.find { it.first == "PHONE_NUMBER" }?.second ?: "")

    //@Test
    fun testValidPhoneNumbers() {
        PhoneNumberTestData.validPhoneNumbers.forEach {
            val values = unitToTest.regex.find(it)?.groupValues
            assertNotNull(values, it)
        }
    }

    //@Test
    fun testInvalidPhoneNumbers() {
        PhoneNumberTestData.invalidPhoneNumbers.forEach {
            val values = unitToTest.regex.find(it)?.groupValues
            assertNull(values, it)
        }
    }
}