package regressionTests

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import regressionTests.config.TestConfig

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
class WarningTest : RegressionTest() {
    @Value("\${test.paths.warning}")
    private val path: String? = null

    @Test
    fun pathShouldNotBeEmpty() {
        Assert.assertNotNull(path)
    }

    @Test
    @Throws(Exception::class)
    fun shouldBeWarnings() {
        val semanticResults = callSemantik(path!!)
        for (results in semanticResults) {
            var found = false
            for ((topicName) in results) {
                if (topicName == "WARNING") {
                    found = true
                }
            }
            Assert.assertTrue(found)
        }
    }
}