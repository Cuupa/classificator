package regressionTests

import com.cuupa.classificator.engine.KnowledgeManager
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import regressionTests.config.ApplicationTestConfiguration
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [ApplicationTestConfiguration::class])
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
class WarningTest : LocalRegressionTest() {

    private val path = "/home/${System.getProperty("user.name")}/testdata/warning"

    @Autowired
    private var knowledgeManager: KnowledgeManager? = null

    /**
     * I don't feel like publishing my private test documents to a public github repo.
     * So just comment it in if its running localy and comment out if pushing
     */
    @Test
    fun testShouldBeWarning() {
        val files = getFilesOfPath(path)
        val contents = files.map { getFileContent(it) }
        var bill = 0
        val list = mutableListOf<String>()
        val millis = measureTimeMillis {
            IntStream.range(0, files.size).forEach { index ->
                val result = knowledgeManager!!.getResults(contents[index])
                assertEquals(1, result.size)
                if ("WARNING" == result.first().topicName) {
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
