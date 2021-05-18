import com.cuupa.classificator.knowledgebase.services.dataExtraction.IbanExtract
import org.junit.jupiter.api.Test

class IbanTest {

    val unitToTest =
        IbanExtract("[a-z]{2}[0-9]{2}[\\s]?[0-9]{4}[\\s]?[0-9]{4}[\\s]?[0-9]{4}[\\s]?[0-9]{4}[\\s]?[0-9]{2}")

    @Test
    fun test() {
        val pattern = unitToTest.regex
        val result = pattern.find(IBANTestData.normalizedIBAN)
        val groupValues = result?.groupValues
        groupValues?.forEach { println(unitToTest.normalize(it)) }

        val result2 = pattern.find(IBANTestData.withoutWhiteSpaces)
        val groupValues2 = result2?.groupValues
        groupValues2?.forEach { println(unitToTest.normalize(it)) }
    }
}