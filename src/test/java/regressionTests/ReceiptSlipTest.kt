package regressionTests

import com.cuupa.classificator.services.Classificator
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
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
class ReceiptSlipTest : RegressionTest() {
    @Autowired
    private val classificator: Classificator? = null
    @Value("\${test.paths.receipt_slip}")
    private val path: String? = null

    @Test
    fun pathShouldNotBeEmpty() {
        Assert.assertNotNull(path)
    }

    @Test
    @Throws(Exception::class)
    fun shouldBeReceiptSlip() {
        val semanticResults = callSemantik(path!!)
        for (results in semanticResults) {
            var found = false
            for ((topicName) in results) {
                if (topicName == "RECEIPT_SLIP") {
                    found = true
                }
            }
            Assert.assertTrue(found)
        }
    }
}