package com.cuupa.classificator.services.stripper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetLocationAndSizeStripper extends PDFTextStripper {

    private volatile boolean textAlreadyParsed = false;

    private List<TextAndPosition> list = new ArrayList<>();

    public GetLocationAndSizeStripper() throws IOException {
        super();
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {

        if (!textAlreadyParsed) {
            TextAndPosition textAndPosition = new TextAndPosition();
            for (TextPosition textPosition : textPositions) {
                if (!isEmpty(textPosition)) {
                    textAndPosition.add(textPosition.getUnicode(), textPosition.getXDirAdj(),
                            textPosition.getWidthDirAdj(), textPosition.getYDirAdj(), textPosition.getHeightDir());
                } else {
                    textAndPosition.add(textPosition.getUnicode(), textPosition.getXDirAdj(),
                            textPosition.getWidthDirAdj(), textPosition.getYDirAdj(), textPosition.getHeightDir());
                    list.add(textAndPosition);
                    textAndPosition = new TextAndPosition();
                }
            }

            if (!list.contains(textAndPosition)) {
                list.add(textAndPosition);
            }
        }
        writeString(text);
    }

    private boolean isEmpty(TextPosition textPosition) {
        return textPosition.getUnicode() == null;
    }

    public void setTextAlreadyParsed() {
        textAlreadyParsed = true;
    }

    public List<TextAndPosition> getTextAndPositions(PDDocument document) throws IOException {
        super.getText(document);
        return list;
    }
}


