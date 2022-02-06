package com.cuupa.classificator

import com.cuupa.classificator.configuration.ApplicationTestConfiguration
import com.cuupa.classificator.engine.services.application.InfoService
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [ApplicationTestConfiguration::class])
@ExtendWith(SpringExtension::class)
@EnableWebSecurity
open class InfoServiceTest {

    @Autowired
    private var unitToTest: InfoService? = null

    //@Test
    fun getVersion() {
        TODO()
        val result = unitToTest?.getVersion()

        assertNotNull(result)
        assertNotEquals("unknown", result)
    }
}