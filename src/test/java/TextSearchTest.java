import com.cuupa.classificator.services.kb.semantic.text.TextSearch;
import org.junit.Test;

import static org.junit.Assert.*;

public class TextSearchTest {

	@Test
	public void shouldFindText() {
		TextSearch text = new TextSearch("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz");
		boolean contains = text.contains("ipsum dolor");
		assertTrue(contains);
	}


	@Test
	public void shouldNotFindText() {
		TextSearch text = new TextSearch("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz");
		boolean contains = text.contains("lorem ipsum dolor");
		assertFalse(contains);
	}

	@Test
	public void shouldHaveThreeOccurences() {
		TextSearch textSearch = new TextSearch(
				"abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz");
		int numberOfOccurences = textSearch.countOccurence("Sparkasse Krefeld");
		assertEquals(3, numberOfOccurences);
	}

	@Test
	public void shouldHaveOneOccurences() {
		TextSearch textSearch = new TextSearch(
				"Sehr geehrte Damen und Herren. Hiermit kündige ich meinen Vertrag bei Ihnen. Mit freundlichen Grüßen, Max Mustermann");
		int numberOfOccurences = textSearch.countOccurence("Vertrag");
		assertEquals(1, numberOfOccurences);

		numberOfOccurences = textSearch.countOccurence("Vertrag");
		assertEquals(1, numberOfOccurences);

		numberOfOccurences = textSearch.countOccurence("Max Mustermann");
		assertEquals(1, numberOfOccurences);

	}
}