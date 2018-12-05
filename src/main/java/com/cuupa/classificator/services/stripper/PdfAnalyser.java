package com.cuupa.classificator.services.stripper;

import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfAnalyser {

    public List<SemanticResult> getResults(final PDDocument document) {

        List<List<TextAndPosition>> textAndPositions = new ArrayList<>();

        for (int pageIndex = 1; pageIndex <= document.getNumberOfPages(); pageIndex++) {
            try {
                GetLocationAndSizeStripper stripper = getStripper(pageIndex, pageIndex);
                textAndPositions.add(stripper.getTextAndPositions(document));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<PageDimension> dimensions = getPageDimensions(document);

        return null;
    }

    private List<PageDimension> getPageDimensions(PDDocument document) {
        return null;
    }

    private GetLocationAndSizeStripper getStripper(int startPage, int endPage) throws IOException {
        GetLocationAndSizeStripper stripper = new GetLocationAndSizeStripper();
        stripper.setStartPage(startPage);
        stripper.setEndPage(endPage);
        return stripper;
    }
}
