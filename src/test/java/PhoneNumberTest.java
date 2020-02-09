import com.cuupa.classificator.services.kb.semantic.dataExtraction.PhoneNumberExtract;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberTest {

    @Test
    public void test() {
        PhoneNumberExtract extract = new PhoneNumberExtract("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$");
        Pattern pattern = extract.getPattern();

        Matcher matcher = pattern.matcher("0301234567");

        if (matcher.find()) {
            //System.out.println(matcher.group());
            System.out.println(extract.normalize(matcher.group()));
        }

        matcher = pattern.matcher("+4930 1234567");
        if (matcher.find()) {
            //System.out.println(matcher.group());
            System.out.println(extract.normalize(matcher.group()));
        }
    }
}
