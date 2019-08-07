package com.cuupa.classificator.services.stripper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationAndSizeStripper extends PDFTextStripper {

    private volatile boolean textAlreadyParsed = false;

    private final List<TextAndPosition> list = new ArrayList<>();

    public LocationAndSizeStripper() throws IOException {
        super();
    }

    @Override
    protected void writeString(String text, @NotNull List<TextPosition> textPositions) throws IOException {
        if (!textAlreadyParsed) {
            parse(textPositions);
            textAlreadyParsed = true;
        }
        writeString(text);
    }

    private void parse(@NotNull List<TextPosition> textPositions) {
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

    private boolean isEmpty(@NotNull TextPosition textPosition) {
        return textPosition.getUnicode() == null || textPosition.getUnicode().equals(" ");
    }

    public void setTextAlreadyParsed() {
        textAlreadyParsed = true;
    }

    @NotNull
    public List<TextAndPosition> getTextAndPositions(PDDocument document) throws IOException {
        super.getText(document);
        return list;
    }
}


