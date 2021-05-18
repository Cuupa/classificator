import com.cuupa.classificator.knowledgebase.KnowledgeManager
import com.cuupa.classificator.monitor.sqlite.EventService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import regressionTests.config.ApplicationTestConfiguration

@SpringBootTest(classes = [ApplicationTestConfiguration::class])
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
class KBTest {

    @Autowired
    private val knowledgeManager: KnowledgeManager? = null

    private val textBill = "Im Anhang finden Sie die Rechnung für den Betrag von 31 €"
    private val textWarning =
        "Im Anhang finden Sie die Rechnung für den Betrag von 31 €. Bisher haben wir noch keine Zahlung erhalten. Dies ist die letzte Mahnung"
    private val textSicknote =
        "Arbeitsunfähigkeitsbescheinigung ICD-10 Code: A10.1 Erstbescheinigung " + "Folgebescheinigung arbeitsunfähig seit 01.01.2020 voraussichtlich arbeitsunfähig bis 01.02.2020 " + "festgestellt am 01.01.2020"

    @Test
    fun parseBill() {
        val resultsBill = knowledgeManager!!.getResults(textBill)
        assertTrue(resultsBill.isNotEmpty())
        assertEquals("BILL", resultsBill[0].topicName)
    }

    @Test
    fun parseWarning() {
        val resultsWarning = knowledgeManager!!.getResults(textWarning)
        assertEquals(1, resultsWarning.size.toLong())
        assertEquals("WARNING", resultsWarning[0].topicName)
    }

    @Test
    fun parseSicknote() {
        val resultsWarning = knowledgeManager!!.getResults(textSicknote)
        assertEquals(1, resultsWarning.size.toLong())
        assertEquals("SICKNOTE", resultsWarning[0].topicName)
    }
}