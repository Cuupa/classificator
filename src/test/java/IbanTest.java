import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.cuupa.classificator.services.kb.semantic.dataExtraction.IbanExtract;

public class IbanTest {

	@Test
	public void test() {
		IbanExtract extract = new IbanExtract();
		Pattern pattern = extract.getPattern();

		Matcher matcher = pattern.matcher("DE19 1234 1234 1234 1234 12");

		if (matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(extract.normalize(matcher.group()));
		}

		matcher = pattern.matcher("DE37123412341234123412");
		if (matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(extract.normalize(matcher.group()));
		}
	}
}
