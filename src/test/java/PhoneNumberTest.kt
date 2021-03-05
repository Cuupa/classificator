import com.cuupa.classificator.knowledgebase.services.dataExtraction.PhoneNumberExtract
import org.junit.jupiter.api.Test

class PhoneNumberTest {

    @Test
    fun test() {
        val extract = PhoneNumberExtract("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")
        val pattern = extract.regex
       // var matcher = pattern.matcher("0301234567")
      //  if (matcher.find()) { //System.out.println(matcher.group());
        //    println(extract.normalize(matcher.group()))
       // }
       // matcher = pattern.matcher("+4930 1234567")
       // if (matcher.find()) { //System.out.println(matcher.group());
       //     println(extract.normalize(matcher.group()))
       // }
    }
}