import com.cuupa.classificator.services.kb.KnowledgeFileParser;
import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.kb.SemanticResult;
import com.cuupa.classificator.services.kb.semantic.Metadata;
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

	private String bill = "BILL = {\r\n" + "	oneOf(\"rechnung\", \"jahresrechnung\"),\r\n"
			+ "	oneOf(\"eur, \"euro\", \"€\"),\r\n" + "	not(\"mahnung\")\r\n" + "}";

	private String warning = "WARNING = {\r\n" + "	oneOf(\"mahnung\", \"zahlungserinnerung\", \"angemahnte Betrag\"),\r\n"
			+ "	oneOf(\"keine Zahlung erhalten\", \"keine Zahlung eingegangen\", \"noch nicht eingegangen\", \"noch nicht bei uns eingegangen\"),\r\n"
			+ "	oneOf(\"eur\", \"euro\", \"€\")\r\n" + "}\r\n" + "\r\n" + "$dueDate = {\r\n"
			+ "	oneOf(\"bis zum [DATE]\")\r\n" + "}\r\n" + "\r\n" + "$IBAN = {\r\n" + "	oneOf(\"IBAN: [IBAN]\")\r\n"
			+ "}";

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
		assertEquals(0, results.size());
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

	@Test
	public void parseSicknote() {
		knowledgeManager.manualParse(KnowledgeFileParser.parseTopic(sickNote));

		List<SemanticResult> results = knowledgeManager.getResults("Krankenkasse bzw. Kostenträger\r\n"
				+ "Techniker Krankenkasse______ 38\r\n" + "Name, Vorname des Versicherten\r\n" + "Thiel\r\n"
				+ "geb. am\r\n" + "Simon 25.09.90\r\n" + "Düsselstr. 63\r\n" + "D 40219 Düsseldorf\r\n"
				+ "Kostenträgerkennung Versicherten-Nr Status\r\n" + "104077501|D038352185 |1\r\n"
				+ "Betriebsstätten-Nr. Arzt-Nr. Datum\r\n" + "240732000 1 600051711 |04.06.18\r\n"
				+ "Arbeitsunfähigkeitsbescheinigung\r\n" + "Erstbescheinigung\r\n" + "Folgebescheinigung\r\n" + "0\r\n"
				+ "□\r\n" + "i\r\n" + "1\r\n" + "□\r\n" + " Arbeitsunfall, Arbeitsunfall- I I dem Durchgangsarzt\r\n"
				+ "folgen, Berufskrankheit I— I zugewiesen\r\n" + "arbeitsunfähig seit 04.06.18\r\n"
				+ "voraussichtlich arbeitsunfähig\r\n" + "bis einschließlich oder letzter 08.06.18\r\n"
				+ "Tag der Arbeitsunfähigkeit\r\n" + "festgestellt am 04.06.18\r\n" + "Ausfertigung für Versicherte\r\n"
				+ "240732000\r\n" + "Dr. med. Heinrich Steinfort\r\n" + "Facharzt f. Chirurgie/Unfallchirurgie\r\n"
				+ "Talip Sakinc\r\n" + "Facharzt f.Orthopädie &\r\n" + "Unfallchirurgie\r\n"
				+ "Kölner kandstr. 11 ( r\\\r\n" + "40591 Düsseldorf\r\n" + "Tel.: 02lfil/215800\r\n"
				+ "Vertragsarztstempel / Unterschrift des Arztes r\r\n" + "AU-begründende Diagnose(n) (ic d -1 0 )\r\n"
				+ "ICD-10 - Code ICD-10 - Code ICD-10 - Code\r\n" + "S60.81 G R S50.81 G L S50.0 G L\r\n"
				+ "ICD-10 - Code ICD-10 - Code ICD-10 - Code\r\n" + "□\r\n"
				+ " sonstiger Unfall, I I VersorgungsUnfallfolgen\r\n" + "I___ I leiden (z.B. BVG)\r\n"
				+ "Es wird die Einleitung folgender besonderer Maßnahmen für erforderlich gehalten\r\n" + "□\r\n"
				+ " Leistungen zur I I stufenweise\r\n" + "medizinischen Rehabilitation I___ I Wiedereingliederung\r\n"
				+ "| l Sonstige\r\n" + "„ I I ab 7. AU-Woche oder I I c . . .\r\n"
				+ "Im Krankengeldfall |__ | sonstiger Krankengeldfall I__ ! Endbescheinigung\r\n"
				+ "Hinweis für Versicherte zum Krankengeld\r\n"
				+ "Achten Sie bei fortbestehender Arbeitsunfähigkeit auf einen lückenlosen Nachweis. Hierfür stellen Sie sich bitte spätestens an\r\n"
				+ "dem Werktag, der auf den letzten Tag der aktuellen Arbeitsunfähigkeitsbescheinigung folgt, bei Ihrem Arzt oder Ihrer Ärztin vor.\r\n"
				+ "Wenn Ihr Arzt oder Ihre Ärztin Ihnen die Bescheinigung für die Krankenkasse aushändigt, müssen Sie diese innerhalb von einer\r\n"
				+ "Woche an Ihre Krankenkasse weiterleiten. Bei verspäteter Vorlage der Bescheinigung bei der Krankenkasse oder lückenhaftem\r\n"
				+ "Nachweis der Arbeitsunfähigkeit droht Krankengeldverlust. Weitere Informationen erhalten Sie bei Ihrer Krankenkasse.\r\n"
				+ "PRF NR Y/9/1410/51/216\r\n" + "Muster 1c/E(1.2018)\r\n" + "vpzlzz (uso) m »raazinH^s saxo");

		for (SemanticResult semanticResult : results) {
			System.out.println(semanticResult.getTopicName());
			System.out.println(semanticResult.getSender());
			List<Metadata> metaData = semanticResult.getMetaData();
			for (Metadata data : metaData) {
				assertNotNull(data.getName());
				assertNotNull(data.getValue());
			}
		}
	}

}
