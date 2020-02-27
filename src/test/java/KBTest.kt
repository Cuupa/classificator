import com.cuupa.classificator.services.kb.KnowledgeFileParser;
import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.kb.SemanticResult;
import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.Topic;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import regressionTests.config.TestConfig;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class KBTest {

	@Autowired
	private KnowledgeManager knowledgeManager;

	@NotNull
	private String bill = "BILL = {\r\n" + "	oneOf(\"rechnung\", \"jahresrechnung\"),\r\n"
			+ "	oneOf(\"eur, \"euro\", \"€\"),\r\n" + "	not(\"mahnung\")\r\n" + "}";

	@NotNull
	private String warning = "WARNING = {\r\n" + "	oneOf(\"mahnung\", \"zahlungserinnerung\", \"angemahnte Betrag\"),\r\n"
			+ "	oneOf(\"keine Zahlung erhalten\", \"keine Zahlung eingegangen\", \"noch nicht eingegangen\", \"noch nicht bei uns eingegangen\"),\r\n"
			+ "	oneOf(\"eur\", \"euro\", \"€\")\r\n" + "}\r\n" + "\r\n" + "$dueDate = {\r\n"
			+ "	oneOf(\"bis zum [DATE]\")\r\n" + "}\r\n" + "\r\n" + "$IBAN = {\r\n" + "	oneOf(\"IBAN: [IBAN]\")\r\n"
			+ "}";

	@NotNull
	private String sickNote = "SICKNOTE = {\r\n" + "	oneOf(\"Arbeitsunfähigkeitsbescheinigung\"),\r\n"
			+ "	oneOf(\"ICD 10\"),\r\n" + "	all(\"Erstbescheinigung\", \"Folgebescheinigung\"),\r\n"
			+ "	all(\"arbeitsunfähig seit\", \"voraussichtlich arbeitsunfähig bis einschließlich oder letzter Tag der Arbeitsunfähigkeit\", \"festgestellt am\")\r\n"
			+ "}";

	@Test
	public void parseTest() {
		KnowledgeFileParser.parseTopic(bill);
	}

	@Test
	public void parseText() {
		knowledgeManager.manualParse(KnowledgeFileParser.parseTopic(bill));

		List<SemanticResult> results = knowledgeManager.getResults("Im Anhang finden Sie die Rechnung für den Betrag von 31€");
		assertTrue(results.size() > 0);

		results = knowledgeManager
				.getResults("Im Anhang finden Sie die Rechnung für den Betrag von 31€. Dies ist die letzte Mahnung");
		assertEquals(1, results.size());
		assertEquals(Topic.OTHER, results.get(0).getTopicName());
	}

	@Test
	public void parseWarning() {
		knowledgeManager.manualParse(KnowledgeFileParser.parseTopic(warning));

		List<SemanticResult> results = knowledgeManager.getResults(
				"Mahnung über 130€ bis zum 31.12.18. Wir haben keine Zahlung erhalten. Bitte überweisen Sie es auf IBAN: DE19 1234 1234 1234 1234 12");

		for (SemanticResult semanticResult : results) {
			List<Metadata> metaData = semanticResult.getMetaData();
			for (Metadata data : metaData) {
				assertNotNull(data.getName());
				assertNotNull(data.getValue());
			}
		}
	}
}
