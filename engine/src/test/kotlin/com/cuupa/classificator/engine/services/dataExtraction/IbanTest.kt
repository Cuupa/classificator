package com.cuupa.classificator.engine.services.dataExtraction

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class IbanTest : ExtractTest() {

    private val unitToTest = IbanExtract(knowledgeFile.regex.find { it.first == "IBAN" }?.second ?: "")

    @Test
    fun testValidIbans() {
        IBANTestData.validIbans.forEach {
            val values = unitToTest.regex.find(it)?.groupValues
            print(values)
            assertNotNull(values, it)
        }
    }

    @Test
    fun testInvalidIbans() {
        IBANTestData.invalidIbans.forEach {
            val values = unitToTest.regex.find(it)?.groupValues
            assertNull(values, it)
        }
    }
}