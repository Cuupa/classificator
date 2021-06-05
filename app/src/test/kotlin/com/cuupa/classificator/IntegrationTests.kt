package com.cuupa.classificator

import com.cuupa.classificator.engine.Classificator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
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
        val result = classificator?.classify(smokeText) ?: listOf()
        assertEquals(1, result.size)
    }

    @Test
    fun shouldHaveTopic() {
        val result = classificator?.classify(smokeText) ?: listOf()
        assertEquals("TERMINATION", result.first().topic)
    }

    @Test
    fun shouldHaveBirthdate() {
        val result = classificator?.classify(smokeText) ?: listOf()
        val dateOfBirth = result.first().metadata.find { it.name == "date_of_birth" }
        assertEquals("01.01.1999", dateOfBirth?.value)
    }

    @Test
    fun shouldHaveIban() {
        val result = classificator?.classify(smokeText) ?: listOf()
        val iban = result.first().metadata.find { it.name == "IBAN" }
        assertEquals("DE19 1234 1234 1234 1234 12", iban?.value)

        val result2 = classificator?.classify(smokeTextExpandedMetadata) ?: listOf()
        val iban2 = result2.first().metadata.find { it.name == "IBAN" }
        assertEquals("DE19 1234 1234 1234 1234 12", iban2?.value)
    }

    @Test
    fun shouldHavePolicyNumber() {
        val result = classificator?.classify(smokeText) ?: listOf()
        val iban = result.first().metadata.find { it.name == "POLICY_NUMBER" }
        assertEquals("32103847298", iban?.value)
    }

    @Test
    fun shouldHavePhoneNumber() {
        val result = classificator?.classify(smokeText) ?: listOf()
        val phoneNumber = result.first().metadata.find { it.name == "phone_Number" }
        assertEquals("+49301234567", phoneNumber?.value)

        val result2 = classificator?.classify(smokeTextExpandedMetadata) ?: listOf()
        val phoneNumber2 = result2.first().metadata.find { it.name == "phone_Number" }
        assertEquals("+49301234567", phoneNumber2?.value)

        val result3 = classificator?.classify(smokeTextExpandedPhoneNumber) ?: listOf()
        val phoneNumber3 = result3.first().metadata.find { it.name == "phone_Number" }
        assertEquals("03331/1234-56", phoneNumber3?.value)
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