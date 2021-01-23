package regressionTests.config

import org.junit.jupiter.api.TestInstance
import java.util.stream.IntStream
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BillTest : LocalRegressionTest() {

    private val path = "/home/${System.getProperty("user.name")}/testdata/bill"

    /**
     * I don't feel like publishing my private test documents to a public github repo.
     * So just comment it in if its running localy and comment out if pushing
     */
    //@Test
    open fun testShouldBeBill() {
        val files = getFilesOfPath(path)
        val contents = files.map { getFileContent(it) }
        var bill = 0
        val list = mutableListOf<String>()
        IntStream.range(0, files.size).forEach { index ->
            val result = knowledgeManager.getResults(contents[index])
            assertEquals(1, result.size)
            if ("BILL" == result.first().topicName) {
                bill += 1
            } else {
                list.add(files[index].absolutePath)
            }
        }

        println("$bill out of ${files.size} recognised")
        println("Not recognised:\n${list.joinToString("\n", "", "")}")
        assertEquals(files.size, bill)
    }
}
