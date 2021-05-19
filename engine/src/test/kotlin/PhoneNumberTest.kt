import com.cuupa.classificator.engine.services.dataExtraction.PhoneNumberExtract
import org.junit.jupiter.api.Test

class PhoneNumberTest {

    val unitToTest = PhoneNumberExtract("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")

    @Test
    fun test() {
        val pattern = unitToTest.regex
        val result = pattern.find(PhoneNumberTestData.test)
        val groupValues = result?.groupValues
        groupValues?.forEach { println(unitToTest.normalize(it)) }

        val result1 = pattern.find(PhoneNumberTestData.germanPrefix)
        val groupValues1 = result1?.groupValues
        groupValues1?.forEach { println(unitToTest.normalize(it)) }
    }
}