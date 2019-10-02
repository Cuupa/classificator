package com.cuupa.classificator.services.kb.semantic.text;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlainText {

    private static final String BLANK = " ";

    private final String plainText;

    private int distance;

    public PlainText(String plainText) {
        this.plainText = plainText;
    }

    public boolean contains(@Nullable String text, int tolerance) {
        try {
            if (text == null || plainText == null) {
                return false;
            }

            if (isTextEqual(text, plainText)) {
                return true;
            }
            text = normalizeText(text);
            String tempPlaintext = normalizeText(plainText);

            if (StringUtils.isEmpty(text)) {
                return false;
            }

            String[] wordsToSearch = text.split(BLANK);

            return search(tempPlaintext, wordsToSearch, tolerance);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isTextEqual(@NotNull String text, @NotNull String plainText) {
        return text.length() == plainText.length() && text.equals(plainText);
    }

    public boolean contains(String text) {
        return contains(text, 1);
    }

    public int countOccurence(String text) {
        if (text == null || plainText == null) {
            return 0;
        }

        text = normalizeText(text);
        String tempPlaintext = normalizeText(plainText);

        String[] wordsToSearch = text.split(BLANK);

        return count(tempPlaintext, wordsToSearch, 1);
    }

    private int count(String tempPlaintext, String[] wordsToSearch, int tolerance) {
        String[] splitPlain = tempPlaintext.split(BLANK);
        int numberOfOccurences = 0;

        SearchInternalLevenshtein searchInternal = new SearchInternalLevenshtein(wordsToSearch, tolerance, splitPlain);

        while (searchInternal.isToSearch()) {
            searchInternal = searchInternal.invoke();

            if (searchInternal.getCurrentPositionSearchString() + 1 > wordsToSearch.length) {
                numberOfOccurences++;
                searchInternal.resetCurrentPositionSearchString();
            }

            if (searchInternal.getCurrentPositionPlainText() + 1 > splitPlain.length || searchInternal.getCurrentPositionSearchString() + 1 > wordsToSearch.length) {
                searchInternal.cancelSearch();
            }
        }

        return numberOfOccurences;
    }

    private boolean search(@NotNull String tempPlaintext, @NotNull String[] wordsToSearch, int tolerance) {
        String[] splitPlain = tempPlaintext.split(BLANK);

        SearchInternalLevenshtein searchInternal = new SearchInternalLevenshtein(wordsToSearch, tolerance, splitPlain);

        while (searchInternal.isToSearch()) {
            searchInternal = searchInternal.invoke();

            if (searchInternal.getCurrentPositionPlainText() + 1 > splitPlain.length || searchInternal.getCurrentPositionSearchString() + 1 > wordsToSearch.length) {
                searchInternal.cancelSearch();
            }
        }
        distance = searchInternal.getDistance();
        return searchInternal.getMatchingWords() == wordsToSearch.length && searchInternal.getDistance() <= tolerance;
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

    public int getDistance() {
        return distance;
    }
}
