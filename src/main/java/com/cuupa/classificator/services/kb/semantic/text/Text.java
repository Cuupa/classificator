package com.cuupa.classificator.services.kb.semantic.text;

import org.jetbrains.annotations.NotNull;

public class Text {

    private static final String BLANK = " ";

    private final String[] stringArray;

    Text(String text) {
        if (text == null) {
            stringArray = new String[0];
        } else {
            text = normalizeText(text);
            stringArray = text.split(BLANK);
        }
    }

    @NotNull
    private String normalizeText(String text) {
        text = text.toLowerCase();
        text = text.replace("\t", BLANK);
        text = text.replace("\n\r", BLANK);
        text = text.replace("\r\n", BLANK);
        text = text.replace("\r", BLANK);
        text = text.replace("\n", BLANK);
        text = text.replace("\t", BLANK);
//		text = text.replace("-", BLANK);
        text = text.replace(",", BLANK);
        text = text.replace(": ", BLANK);
        text = text.replace("€", " €");
        text = text.replace("Ãœ", "ae");
        text = text.replace("ä", "ae");
        text = text.replace("ã¼", "ue");
        text = text.replace("ü", "ue");
        text = text.replace("/", BLANK);
        text = text.replace("_", BLANK);
        text = text.replaceAll(" {2}", " ");
        return text.trim();
    }

    int length() {
        if (stringArray != null) {
            return stringArray.length;
        }
        return 0;
    }

    public String get(int currentPositionSearchString) {
        return stringArray[currentPositionSearchString];
    }

    boolean isEmpty() {
        return stringArray == null || stringArray.length == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof String[])) {
            return false;
        }

        String[] compare = (String[]) obj;
        if (stringArray == compare) {
            return true;
        }

        if (stringArray.length != compare.length) {
            return false;
        }

        for (int i = 0; i < stringArray.length; i++) {
            if (!stringArray[i].equals(compare[i])) {
                return false;
            }
        }
        return true;
    }
}
