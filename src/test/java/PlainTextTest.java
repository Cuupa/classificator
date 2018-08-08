import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cuupa.classificator.services.kb.semantic.PlainText;

public class PlainTextTest {

	@Test
	public void shouldWork() {
		PlainText text = new PlainText("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz");
		boolean contains = text.contains("ipsum dolor");
		assertTrue(contains);
	}
}
