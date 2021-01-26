package regressionTests

import com.cuupa.classificator.services.kb.KnowledgeManager
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import regressionTests.config.TestConfig
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
open class SEPATest : LocalRegressionTest() {

    private val path = "/home/${System.getProperty("user.name")}/testdata/SEPA"

    @Autowired
    private var knowledgeManager: KnowledgeManager? = null

    /**
     * I don't feel like publishing my private test documents to a public github repo.
     * So just comment it in if its running localy and comment out if pushing
     */
    @Test
    open fun testShouldBeBill() {
        val files = getFilesOfPath(path)
        val contents = files.map { getFileContent(it) }
        var bill = 0
        val list = mutableListOf<String>()
        val millis = measureTimeMillis {
            IntStream.range(0, files.size).forEach { index ->
                val result = knowledgeManager!!.getResults(contents[index])
                assertEquals(1, result.size)
                if ("SEPA" == result.first().topicName) {
                    bill += 1
                } else {
                    list.add("${files[index].absolutePath} classified as ${result.first().topicName}")
                }
            }
        }

        println("$bill out of ${files.size} recognised")
        println("Recognizing of ${files.size} texts took $millis ms")
        println("Not recognised:\n${list.joinToString("\n", "", "")}")
        assertEquals(files.size, bill)
    }
}