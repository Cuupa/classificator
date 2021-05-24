package com.cuupa.classificator

import com.cuupa.classificator.engine.Classificator
import org.junit.jupiter.api.Test
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
class IntegrationTests {

    @Autowired
    private val classificator: Classificator? = null

    @Test
    fun shouldContextLoad() {

    }

    @Test
    fun shouldClassifySmokeText() {
        val result = classificator?.classify(smokeText) ?: listOf()
        assertEquals(1, result.size)
        val (topicname, sender, metaData) = result.first()
        assertEquals("TERMINATION", topicname)

        val dateOfBirth = metaData.find { it.name == "DATE_OF_BIRTH" }
        assertEquals("01.01.1999", dateOfBirth?.value)

        val iban = metaData.find { it.name == "IBAN" }
        assertEquals("DE19 1234 1234 1234 1234 12", iban?.value)

        val policyNumber = metaData.find { it.name == "POLICY_NUMBER" }
        assertEquals("32103847298", policyNumber?.value)
    }

    companion object{
        val smokeText = """Sehr geehrte Damen und Herren,
            hiermit kündige ich, Max Mustermann, geboren am 01.01.1999, meinen Vertrag zur Vertrangsnummer 32103847298 zum 31.12.3030.
            Bitte überweisen Sie den verbleibenden Betrag auf mein Konto DE19 1234 1234 1234 1234 12.
        """.trimMargin()
    }
}