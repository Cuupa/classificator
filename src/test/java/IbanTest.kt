import com.cuupa.classificator.knowledgebase.services.dataExtraction.IbanExtract
import org.junit.jupiter.api.Test

class IbanTest {

    @Test
    fun test() {
        val extract = IbanExtract("[a-z]{2}[0-9]{2}[\\s]?[0-9]{4}[\\s]?[0-9]{4}[\\s]?[0-9]{4}[\\s]?[0-9]{4}[\\s]?[0-9]{2}")
        val pattern = extract.pattern
        var matcher = pattern.matcher("DE19 1234 1234 1234 1234 12")
        if (matcher.find()) {
            println(matcher.group())
            println(extract.normalize(matcher.group()))
        }
        matcher = pattern.matcher("DE37123412341234123412")
        if (matcher.find()) {
            println(matcher.group())
            println(extract.normalize(matcher.group()))
        }
    }
}