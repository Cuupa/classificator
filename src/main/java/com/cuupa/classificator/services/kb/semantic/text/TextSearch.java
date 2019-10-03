package com.cuupa.classificator.services.kb.semantic.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextSearch {

    private static final String BLANK = " ";

    private final PlainText plainText;

    private int distance;

    public TextSearch(PlainText plainText) {
        this.plainText = plainText;
    }

    public boolean contains(@Nullable String text, int tolerance) {
        try {

            SearchText searchText = new SearchText(text);

            if (searchText.isEmpty() || plainText.isEmpty()) {
                return false;
            }

            if (searchText.equals(plainText)) {
                return true;
            }

            return search(plainText, searchText, tolerance);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contains(String text) {
        return contains(text, 1);
    }

    public int countOccurence(String text) {
        SearchText searchText = new SearchText(text);

        if (searchText.isEmpty() || plainText.isEmpty()) {
            return 0;
        }

        return count(plainText, searchText, 1);
    }

    private int count(PlainText plainText, SearchText wordsToSearch, int tolerance) {
        int numberOfOccurences = 0;

        SearchInternalLevenshtein searchInternal = new SearchInternalLevenshtein(wordsToSearch, plainText, tolerance);

        while (searchInternal.isToSearch()) {
            searchInternal = searchInternal.invoke();

            if (searchInternal.getCurrentPositionSearchString() + 1 > wordsToSearch.length()) {
                numberOfOccurences++;
                searchInternal.resetCurrentPositionSearchString();
            }

            if (searchInternal.getCurrentPositionPlainText() + 1 > plainText.length() || searchInternal.getCurrentPositionSearchString() + 1 > wordsToSearch
                    .length()) {
                searchInternal.cancelSearch();
            }
        }

        return numberOfOccurences;
    }

    private boolean search(PlainText tempPlaintext, @NotNull SearchText wordsToSearch, int tolerance) {

        SearchInternalLevenshtein searchInternal = new SearchInternalLevenshtein(wordsToSearch,
                                                                                 tempPlaintext,
                                                                                 tolerance);

        while (searchInternal.isToSearch()) {
            searchInternal = searchInternal.invoke();

            if (searchInternal.getCurrentPositionPlainText() + 1 > tempPlaintext.length() || searchInternal.getCurrentPositionSearchString() + 1 > wordsToSearch
                    .length()) {
                searchInternal.cancelSearch();
            }
        }
        distance = searchInternal.getDistance();
        return searchInternal.getMatchingWords() == wordsToSearch.length() && searchInternal.getDistance() <= tolerance;
    }

    public int getDistance() {
        return distance;
    }
}
