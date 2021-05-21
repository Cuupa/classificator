import com.cuupa.classificator.engine.services.dataExtraction.IbanExtract
import com.cuupa.classificator.engine.services.kb.KnowledgeFileExtractor
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class IbanTest {

    private val unitToTest = IbanExtract(knowledgeFile.regex.find { it.first == "IBAN" }?.second ?: "")

    @Test
    fun testValidIbans() {
        IBANTestData.validIbans.forEach {
            val values = unitToTest.regex.find(it)?.groupValues
            assertNotNull(values, it)
        }
    }

    @Test
    fun testInvalidIbans() {
        IBANTestData.invalidIbans.forEach {
            val values = unitToTest.regex.find(it)?.groupValues
            assertNull(values, it)
        }
    }

    companion object {
        val knowledgeFile = KnowledgeFileExtractor.extractKnowledgebase(File("../build/knowledgebase/kb-DEV.db"))
    }
}