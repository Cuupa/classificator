package com.cuupa.classificator.services;

import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.kb.SemanticResult;
import com.cuupa.classificator.services.stripper.PdfAnalyser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Classificator {

    private final KnowledgeManager manager;

    private final PdfAnalyser analyser;

    public Classificator(KnowledgeManager manager, PdfAnalyser analyser) {
		this.manager = manager;
        this.analyser = analyser;
    }

    public List<SemanticResult> classify(final String text) {
		if(text == null || text.length() == 0) {
			return new ArrayList<>();
		}
		
		return manager.getResults(text);
	}

    public List<SemanticResult> classify(final byte[] content) {
        final List<SemanticResult> results = new ArrayList<>();
        if (content == null || content.length == 0) {
            return new ArrayList<>();
        }
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream((content)))) {
            String text = extractText(document).stream().collect(Collectors.joining());
            results.addAll(manager.getResults(text));
            List<SemanticResult> resultFromStructure = analyser.getResults(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private List<String> extractText(final PDDocument document) {
        final List<String> pages = new ArrayList<>();
        try {
            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                pages.add(stripper.getText(document));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages;
    }
}
