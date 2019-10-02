import com.cuupa.classificator.services.kb.semantic.text.PlainText;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlainTextTest {

	@Test
	public void shouldFindText() {
		PlainText text = new PlainText("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz");
		boolean contains = text.contains("ipsum dolor");
		assertTrue(contains);
	}


	@Test
	public void shouldNotFindText() {
		PlainText text = new PlainText("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz");
		boolean contains = text.contains("lorem ipsum dolor");
		assertFalse(contains);
	}

	@Test
	public void shouldHaveThreeOccurences() {
		PlainText plainText = new PlainText(
				"abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz");
		int numberOfOccurences = plainText.countOccurence("Sparkasse Krefeld");
		assertEquals(3, numberOfOccurences);
	}

	@Test
	public void shouldHaveOneOccurences() {
		PlainText plainText = new PlainText(
				"Sehr geehrte Damen und Herren. Hiermit kündige ich meinen Vertrag bei Ihnen. Mit freundlichen Grüßen, Max Mustermann");
		int numberOfOccurences = plainText.countOccurence("Vertrag");
		assertEquals(1, numberOfOccurences);

		numberOfOccurences = plainText.countOccurence("Vertrag");
		assertEquals(1, numberOfOccurences);

		numberOfOccurences = plainText.countOccurence("Max Mustermann");
		assertEquals(1, numberOfOccurences);

	}
}
