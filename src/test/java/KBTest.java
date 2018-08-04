

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.cuupa.classificator.services.kb.KnowledgeFileParser;
import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;

public class KBTest {
	
	String bill ="BILL = {\r\n" + 
			"	oneOf(\"rechnung\", \"jahresrechnung\"),\r\n" + 
			"	oneOf(\"eur, \"euro\", \"€\"),\r\n" + 
			"	not(\"mahnung\")\r\n" + 
			"}";

	String warning = "WARNING = {\r\n" + 
			"	oneOf(\"mahnung\", \"zahlungserinnerung\"),\r\n" + 
			"	oneOf(\"keine Zahlung erhalten\", \"keine Zahlung eingegangen\"),\r\n" + 
			"	oneOf(\"eur\", \"euro\", \"€\")\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"$dueDate = {\r\n" + 
			"	oneOf(\"bis zum [DATE]\", \"bis zum [DATE]\"\r\n" + 
			"}";
	
	@Test
	public void parseTest() {
		KnowledgeFileParser.parse(bill);
	}
	
	@Test
	public void parseText() {
		KnowledgeManager manager = new KnowledgeManager();
		manager.manualParse(KnowledgeFileParser.parse(bill));
		
		List<SemanticResult> results = manager.getResults("Im Anhang finden Sie die Rechnung für den Betrag von 31€");
		assertTrue(results.size() > 0);
		
		results = manager.getResults("Im Anhang finden Sie die Rechnung für den Betrag von 31€. Dies ist die letzte Mahnung");
		assertTrue(results.size() == 0);
	}
	
	@Test
	public void parseWarning() {
		KnowledgeManager manager = new KnowledgeManager();
		manager.manualParse(KnowledgeFileParser.parse(warning));
		
		List<SemanticResult> results = manager.getResults("Mahnung über 130€ bis zum 31.12.18. Wir haben keine Zahlung erhalten.");
	}
	
}
