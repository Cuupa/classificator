package com.cuupa.classificator.services.stripper;

import com.cuupa.classificator.services.kb.SemanticResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfAnalyser {

    @Nullable
    public List<SemanticResult> getResults(@NotNull final PDDocument document) {

        List<List<TextAndPosition>> textAndPositions = new ArrayList<>();

        for (int pageIndex = 1; pageIndex <= document.getNumberOfPages(); pageIndex++) {
            try {
                LocationAndSizeStripper stripper = getStripper(pageIndex, pageIndex);
                textAndPositions.add(stripper.getTextAndPositions(document));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @NotNull
    private LocationAndSizeStripper getStripper(int startPage, int endPage) throws IOException {
        LocationAndSizeStripper stripper = new LocationAndSizeStripper();
        stripper.setStartPage(startPage);
        stripper.setEndPage(endPage);
        return stripper;
    }
}
