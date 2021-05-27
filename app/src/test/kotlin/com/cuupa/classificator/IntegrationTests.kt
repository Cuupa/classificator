package com.cuupa.classificator

import com.cuupa.classificator.domain.SemanticResult
import com.cuupa.classificator.engine.Classificator
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * @author Simon Thiel (https://github.com/cuupa) (23.05.2021)
 */
@SpringBootTest(classes = [ApplicationTestConfiguration::class])
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTests {

    @Autowired
    private val classificator: Classificator? = null

    private var result = listOf<SemanticResult>()

    @BeforeAll
    fun getResult() {
        result = classificator?.classify(smokeText) ?: listOf()
    }

    @Test
    fun shouldContextLoad() {
        assertNotNull(classificator)
    }

    @Test
    fun shouldHaveOneResult() {
        assertEquals(1, result.size)
    }

    @Test
    fun shouldHaveTopic() {
        assertEquals("TERMINATION", result.first().topicName)
    }

    @Test
    fun shouldHaveBirthdate() {
        val dateOfBirth = result.first().metadata.find { it.name == "date_of_birth" }
        assertEquals("01.01.1999", dateOfBirth?.value)
    }

    //@Test
    fun shouldHaveIban() {
        val iban = result.first().metadata.find { it.name == "IBAN" }
        assertEquals("DE19 1234 1234 1234 1234 12", iban?.value)
    }

    @Test
    fun shouldHavePolicyNumber() {
        val iban = result.first().metadata.find { it.name == "POLICY_NUMBER" }
        assertEquals("32103847298", iban?.value)
    }

    @Test
    fun shouldHavePhoneNumber() {
        val phoneNumber = result.first().metadata.find { it.name == "phone_Number" }
        assertEquals("+49301234567", phoneNumber?.value)
    }

    @Test
    fun shouldHaveLanguage() {
        val iban = result.first().metadata.find { it.name == "language" }
        assertEquals("de", iban?.value)
    }

    companion object {
        val smokeText = """Sehr geehrte Damen und Herren,
            |hiermit kündige ich, Max Mustermann, geboren am 01.01.1999, meinen Vertrag zur Vertrangsnummer 32103847298 zum 31.12.3030.
            |Bitte überweisen Sie den verbleibenden Betrag auf mein Konto mit der IBAN DE19123412341234123412.
            |Bei Rückfragen stehe ich unter der Tel: +49301234567 zur Verfügung
        """.trimMargin()
    }
}