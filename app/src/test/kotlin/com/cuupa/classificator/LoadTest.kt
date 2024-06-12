package com.cuupa.classificator

import com.cuupa.classificator.configuration.ApplicationTestConfiguration
import com.cuupa.classificator.engine.Classificator
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@SpringBootTest(classes = [ApplicationTestConfiguration::class])
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableWebSecurity
open class LoadTest {

    @Autowired
    private val classificator: Classificator? = null

    @Test
    fun shouldSucceedWith10Documents() {
        val numberOfDocuuments = 10
        log.error("Running LoadTest with $numberOfDocuuments documents")
        for (i in 0..numberOfDocuuments) {
            execute()
        }
        log.error("Succesfully run LoadTest with $numberOfDocuuments documents")
    }

    @Test
    fun shouldSucceedWith20Documents() {
        val numberOfDocuuments = 20
        log.error("Running LoadTest with $numberOfDocuuments documents")
        for (i in 0..numberOfDocuuments) {
            execute()
        }
        log.error("Succesfully run LoadTest with $numberOfDocuuments documents")
    }

    @Test
    fun shouldSucceedWith50Documents() {
        val numberOfDocuuments = 50
        log.error("Running LoadTest with $numberOfDocuuments documents")
        for (i in 0..numberOfDocuuments) {
            execute()
        }
        log.error("Succesfully run LoadTest with $numberOfDocuuments documents")
    }

    @Test
    fun shouldSucceedWith100Documents() {
        val numberOfDocuuments = 100
        log.error("Running LoadTest with $numberOfDocuuments documents")
        for (i in 0..numberOfDocuuments) {
            execute()
        }
        log.error("Succesfully run LoadTest with $numberOfDocuuments documents")
    }

    private fun execute() {
        val result = classificator?.classify(smokeText)
        assertNotNull(result)
        assertEquals(1, result.size)
    }

    companion object {
        private val log: Log = LogFactory.getLog(LoadTest::class.java)
        private val smokeText = """Sehr geehrte Damen und Herren,
            hiermit kündige ich, Max Mustermann, geboren am 01.01.1999, meinen Vertrag zur Vertrangsnummer 32103847298 zum 31.12.3030.
            Bitte überweisen Sie den verbleibenden Betrag auf mein Konto mit der IBAN DE19123412341234123412.
            Bei Rückfragen stehe ich unter der Tel: +49301234567 zur Verfügung
        """.trimIndent()
    }
}