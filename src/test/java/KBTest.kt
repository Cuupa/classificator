import com.cuupa.classificator.services.kb.KnowledgeFileParser.parseTopic
import com.cuupa.classificator.services.kb.KnowledgeManager
import com.cuupa.classificator.services.kb.semantic.Topic
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import regressionTests.config.TestConfig

@SpringBootTest(classes = [TestConfig::class])
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
class KBTest {
    @Autowired
    private val knowledgeManager: KnowledgeManager? = null
    private val bill = ("BILL = {\r\n" + "	oneOf(\"rechnung\", \"jahresrechnung\"),\r\n"
            + "	oneOf(\"eur, \"euro\", \"€\"),\r\n" + "	not(\"mahnung\")\r\n" + "}")
    private val warning = ("WARNING = {\r\n" + "	oneOf(\"mahnung\", \"zahlungserinnerung\", \"angemahnte Betrag\"),\r\n"
            + "	oneOf(\"keine Zahlung erhalten\", \"keine Zahlung eingegangen\", \"noch nicht eingegangen\", \"noch nicht bei uns eingegangen\"),\r\n"
            + "	oneOf(\"eur\", \"euro\", \"€\")\r\n" + "}\r\n" + "\r\n" + "\$dueDate = {\r\n"
            + "	oneOf(\"bis zum [DATE]\")\r\n" + "}\r\n" + "\r\n" + "\$IBAN = {\r\n" + "	oneOf(\"IBAN: [IBAN]\")\r\n"
            + "}")
    private val sickNote = ("SICKNOTE = {\r\n" + "	oneOf(\"Arbeitsunfähigkeitsbescheinigung\"),\r\n"
            + "	oneOf(\"ICD 10\"),\r\n" + "	all(\"Erstbescheinigung\", \"Folgebescheinigung\"),\r\n"
            + "	all(\"arbeitsunfähig seit\", \"voraussichtlich arbeitsunfähig bis einschließlich oder letzter Tag der Arbeitsunfähigkeit\", \"festgestellt am\")\r\n"
            + "}")

    @Test
    fun parseTest() {
        parseTopic(bill)
    }

    @Test
    fun parseText() {
        knowledgeManager!!.manualParse(parseTopic(bill))
        var results = knowledgeManager.getResults("Im Anhang finden Sie die Rechnung für den Betrag von 31€")
        Assert.assertTrue(results.size > 0)
        results = knowledgeManager
                .getResults("Im Anhang finden Sie die Rechnung für den Betrag von 31€. Dies ist die letzte Mahnung")
        Assert.assertEquals(1, results.size.toLong())
        Assert.assertEquals(Topic.OTHER, results[0].topicName)
    }

    @Test
    fun parseWarning() {
        knowledgeManager!!.manualParse(parseTopic(warning))
        val results = knowledgeManager.getResults(
                "Mahnung über 130€ bis zum 31.12.18. Wir haben keine Zahlung erhalten. Bitte überweisen Sie es auf IBAN: DE19 1234 1234 1234 1234 12")
        for ((_, _, metaData) in results) {
            for ((name, value) in metaData) {
                Assert.assertNotNull(name)
                Assert.assertNotNull(value)
            }
        }
    }
}