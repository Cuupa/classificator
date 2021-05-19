package com.cuupa.classificator.externalconfiguration

import com.cuupa.classificator.externalconfiguration.configuration.ExternalConfigurationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


/**
 * @author Simon Thiel (https://github.com/cuupa) (19.05.2021)
 */
@SpringBootTest(classes = [ExternalConfigurationTest::class])
@ExtendWith(SpringExtension::class)
class ConfigTest {

    @Autowired
    private val unitToTest: Config? = null

    @Test
    fun shouldLoadContext() {
    }

    @Test
    fun shouldNotBeNullRoot() {
        assertNotNull(unitToTest)
    }

    @Test
    fun shouldNotBeNullClassificator() {
        assertNotNull(unitToTest?.classificator)
    }

    @Test
    fun shouldNotBeNullKnowledgebase() {
        assertNotNull(unitToTest?.classificator?.knowledgeBase)
    }

    @Test
    fun shouldNotBeNullMonitorConfig() {
        assertNotNull(unitToTest?.classificator?.monitorConfig)
    }

    @Test
    fun shouldNotBeNullMonitorConfigUsername() {
        assertNotNull(unitToTest?.classificator?.monitorConfig?.username)
    }

    @Test
    fun shouldNotBeNullMonitorConfigPassword() {
        assertNotNull(unitToTest?.classificator?.monitorConfig?.password)
    }

    @Test
    fun shouldNotBeNullMonitorConfigEnabled() {
        assertNotNull(unitToTest?.classificator?.monitorConfig?.enabled)
    }

    @Test
    fun shouldNotBeNullMonitorConfigLogText() {
        assertNotNull(unitToTest?.classificator?.monitorConfig?.enabled)
    }

    @Test
    fun shouldNotBeNullMonitorConfigDatabasename() {
        assertNotNull(unitToTest?.classificator?.monitorConfig?.databaseName)
    }

    @Test
    fun shouldKnowledgebaseBeValid() {
        assertEquals(unitToTest?.classificator?.knowledgeBase, "knowledgebase")
    }

    @Test
    fun shouldUsernameBeValid() {
        assertEquals(unitToTest?.classificator?.monitorConfig?.username, "John")
    }

    @Test
    fun shouldOasswordBeValid() {
        assertEquals(unitToTest?.classificator?.monitorConfig?.password, "Doe")
    }

    @Test
    fun shouldMonitorEnabledBeValid() {
        assertEquals(unitToTest?.classificator?.monitorConfig?.enabled, true)
    }

    @Test
    fun shouldLogTextBeValid() {
        assertEquals(unitToTest?.classificator?.monitorConfig?.logText, true)
    }

    @Test
    fun shouldDatabasenameBeValid() {
        assertEquals(unitToTest?.classificator?.monitorConfig?.databaseName, "monitor.db")
    }
}