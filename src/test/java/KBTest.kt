import com.cuupa.classificator.services.kb.KnowledgeFileParser.parseTopic
import com.cuupa.classificator.services.kb.KnowledgeManager
import com.cuupa.classificator.services.kb.TopicService
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import regressionTests.config.TestConfig
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
class KBTest {

    @Autowired
    private val knowledgeManager: KnowledgeManager? = null

    @Autowired
    private val topicService: TopicService? = null

    private val bill = Files.readString(Paths.get("src/main/resources/kbfiles/bill.dsl"))
    private val warning = Files.readString(Paths.get("src/main/resources/kbfiles/warning.dsl"))
    private val sickNote = Files.readString(Paths.get("src/main/resources/kbfiles/sicknote.dsl"))

    private val textBill = "Im Anhang finden Sie die Rechnung für den Betrag von 31 €"
    private val textWarning =
        "Im Anhang finden Sie die Rechnung für den Betrag von 31 €. Bisher haben wir noch keine Zahlung erhalten. Dies ist die letzte Mahnung"
    private val textSicknote =
        "Arbeitsunfähigkeitsbescheinigung ICD-10 Code: A10.1 Erstbescheinigung " + "Folgebescheinigung arbeitsunfähig seit 01.01.2020 voraussichtlich arbeitsunfähig bis 01.02.2020 " + "festgestellt am 01.01.2020"

    @Test
    fun parseBill() {
        knowledgeManager!!.manualParse(parseTopic(bill))
        val resultsBill = knowledgeManager.getResults(textBill)
        Assert.assertTrue(resultsBill.isNotEmpty())
        Assert.assertEquals("BILL", resultsBill[0].topicName)
    }

    @Test
    fun parseWarning() {
        knowledgeManager!!.manualParse(parseTopic(warning))
        val resultsWarning = knowledgeManager.getResults(textWarning)
        Assert.assertEquals(1, resultsWarning.size.toLong())
        Assert.assertEquals("WARNING", resultsWarning[0].topicName)
    }

    @Test
    fun parseSicknote() {
        knowledgeManager!!.manualParse(parseTopic(sickNote))
        val resultsWarning = knowledgeManager.getResults(textSicknote)
        Assert.assertEquals(1, resultsWarning.size.toLong())
        Assert.assertEquals("SICKNOTE", resultsWarning[0].topicName)
    }
}