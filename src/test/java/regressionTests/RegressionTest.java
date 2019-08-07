package regressionTests;

import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.SemanticResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class RegressionTest {

    @Autowired
    private Classificator classificator;

    @NotNull
    protected List<List<SemanticResult>> callSemantik(@NotNull String path) throws IOException {
        List<List<String>> contents = Files.list(Paths.get(path)).map(this::read).collect(Collectors.toList());
        List<List<SemanticResult>> semanticResults = new ArrayList<>();
        for (List<String> string : contents) {
            String strings = string.stream().map(Objects::toString).collect(Collectors.joining());
            semanticResults.add(classificator.classify(strings));
        }

        return semanticResults;
    }

    @NotNull
    protected List<List<SemanticResult>> callSemantikWithStructure(@NotNull String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        List<List<SemanticResult>> semanticResults = new ArrayList<>();
        semanticResults.add(classificator.classify(bytes));

        return semanticResults;
    }

    @NotNull
    protected List<String> read(@NotNull Path path) {
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
