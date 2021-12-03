package com.cuupa.classificator

import com.cuupa.classificator.configuration.ApplicationTestConfiguration
import com.cuupa.classificator.engine.Classificator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals

/**
 * @author Simon Thiel (https://github.com/cuupa) (23.05.2021)
 */
@SpringBootTest(classes = [ApplicationTestConfiguration::class])
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTests {

    @Autowired
    private val classificator: Classificator? = null

    @Test
    fun shouldContextLoad() {

    }

    @Test
    fun shouldHaveOneResult() {
        try {
            val result = classificator?.classify(smokeText) ?: listOf()
            assertEquals(1, result.size)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    @Test
    fun shouldHaveTopic() {
        try {
            val result = classificator?.classify(smokeText) ?: listOf()
            assertEquals("TERMINATION", result.first().topic)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    @Test
    fun shouldHaveBirthdate() {
        try {
            val result = classificator?.classify(smokeText) ?: listOf()
            val dateOfBirth = result.first().metadata.find { it.name == "date_of_birth" }
            assertEquals("01.01.1999", dateOfBirth?.value)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    @Test
    fun shouldHaveIban() {
        try {
            val result = classificator?.classify(smokeText) ?: listOf()
            val iban = result.first().metadata.find { it.name == "IBAN" }
            assertEquals("DE19 1234 1234 1234 1234 12", iban?.value)
        } catch (e: Exception) {
            fail(e.message)
        }
        try {
            val result2 = classificator?.classify(smokeTextExpandedMetadata) ?: listOf()
            val iban2 = result2.first().metadata.find { it.name == "IBAN" }
            assertEquals("DE19 1234 1234 1234 1234 12", iban2?.value)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    @Test
    fun shouldHavePolicyNumber() {
        try {
            val result = classificator?.classify(smokeText) ?: listOf()
            val iban = result.first().metadata.find { it.name == "POLICY_NUMBER" }
            assertEquals("32103847298", iban?.value)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    @Test
    fun shouldHavePhoneNumber() {
        try {
            val result = classificator?.classify(smokeText) ?: listOf()
            val phoneNumber = result.first().metadata.find { it.name == "phone_Number" }
            assertEquals("+49301234567", phoneNumber?.value)
        } catch (e: Exception) {
            fail(e.message)
        }

        try {
            val result = classificator?.classify(smokeTextExpandedMetadata) ?: listOf()
            val phoneNumber = result.first().metadata.find { it.name == "phone_Number" }
            assertEquals("+49301234567", phoneNumber?.value)
        } catch (e: Exception) {
            fail(e.message)
        }

        try {
            val result = classificator?.classify(smokeTextExpandedPhoneNumber) ?: listOf()
            val phoneNumber = result.first().metadata.find { it.name == "phone_Number" }
            assertEquals("03331/1234-56", phoneNumber?.value)
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    companion object {
        val smokeText = """Sehr geehrte Damen und Herren,
            hiermit kündige ich, Max Mustermann, geboren am 01.01.1999, meinen Vertrag zur Vertrangsnummer 32103847298 zum 31.12.3030.
            Bitte überweisen Sie den verbleibenden Betrag auf mein Konto mit der IBAN DE19123412341234123412.
            Bei Rückfragen stehe ich unter der Tel: +49301234567 zur Verfügung
        """.trimIndent()

        val smokeTextExpandedMetadata = """Sehr geehrte Damen und Herren,
            hiermit kündige ich, Max Mustermann, geboren am 01.01.1999, meinen Vertrag zur Vertrangsnummer 32103847298 zum 31.12.3030.
            Bitte überweisen Sie den verbleibenden Betrag auf mein Konto mit der IBAN DE19 1234 1234 1234 1234 12.
            Bei Rückfragen stehe ich unter der Tel: +4930 1234567 zur Verfügung
        """.trimIndent()

        val smokeTextExpandedPhoneNumber = """Sehr geehrte Damen und Herren,
            hiermit kündige ich, Max Mustermann, geboren am 01.01.1999, meinen Vertrag zur Vertrangsnummer 32103847298 zum 31.12.3030.
            Bitte überweisen Sie den verbleibenden Betrag auf mein Konto mit der IBAN DE19 1234 1234 1234 1234 12.
            Bei Rückfragen stehe ich unter der Tel: 0 33 31 /12 34 - 56 zur Verfügung
        """.trimIndent()
    }
}