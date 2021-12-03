package regressionTests

import com.cuupa.classificator.engine.ClassificatorImplementation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import regressionTests.config.EngineTestConfiguration
import kotlin.test.assertNotNull

/**
 * @author Simon Thiel (https://github.com/cuupa) (31.05.2021)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [EngineTestConfiguration::class])
@ExtendWith(SpringExtension::class)
class SmokeTest {

    @Autowired
    private var unitToTest: ClassificatorImplementation? = null

    @Test
    fun shouldContextStart() {
        assertNotNull(unitToTest)
    }

    @Test
    fun shouldHaveNoError() {
        var result = unitToTest?.classify("Test")
        assertNotNull(result)

        result = unitToTest?.classify("test")
        assertNotNull(result)

        result = unitToTest?.classify("test=")
        assertNotNull(result)

        result = unitToTest?.classify("Test=")
        assertNotNull(result)
    }
}
