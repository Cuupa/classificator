package regressionTests;

import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import regressionTests.config.TestConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class WarningTest {

    @Autowired
    private Classificator classificator;

    @Value("${test.paths.warning}")
    private String path;

    @Test
    public void pathShouldNotBeEmpty() {
        assertNotNull(path);
    }

    @Test
    public void shouldBeWarnings() throws Exception {
        List<List<String>> contents = Files.list(Paths.get(path)).map(this::read).collect(Collectors.toList());
        List<List<SemanticResult>> semanticResults = new ArrayList<>();
        for (List<String> string : contents) {
            String strings = contents.stream().map(Objects::toString).collect(Collectors.joining());
            semanticResults.add(classificator.classify(strings));
        }

        for (List<SemanticResult> results : semanticResults) {
            boolean found = false;
            for (SemanticResult result : results) {
                if (result.getTopicName().equals("WARNING")) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    private List<String> read(Path path) {
        try (PDDocument document = PDDocument.load(Files.readAllBytes(path))) {
            List<String> pages = new ArrayList<>(document.getNumberOfPages());
            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                pages.add(stripper.getText(document));
            }

            return pages;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return Collections.emptyList();
    }
}
