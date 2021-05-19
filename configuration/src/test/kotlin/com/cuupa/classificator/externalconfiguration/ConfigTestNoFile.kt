package com.cuupa.classificator.externalconfiguration

import com.cuupa.classificator.externalconfiguration.configuration.ExternalConfigurationEmptyTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertNotNull
import kotlin.test.assertNull


/**
 * @author Simon Thiel (https://github.com/cuupa) (19.05.2021)
 */
@SpringBootTest(classes = [ExternalConfigurationEmptyTest::class])
@ExtendWith(SpringExtension::class)
@ActiveProfiles(profiles = ["no-file"])
class ConfigTestNoFile {

    @Autowired
    private val unitToTest: Config? = null

    @Test
    fun shouldLoadContext() {

    }

    @Test
    fun shouldBeNotNull() {
        assertNotNull(unitToTest)
    }

    @Test
    fun shouldBeEmptyConfig() {
        assertNull(unitToTest?.classificator)
    }
}