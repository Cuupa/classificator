import com.cuupa.classificator.services.kb.semantic.PlainText;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
