package com.cuupa.classificator.services.kb.semantic.text;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.NotNull;

public class SearchInternalLevenshtein {

    private final String[] splitPlain;

    private String[] wordsToSearch;

    private int tolerance;

    private int currentPositionPlainText;

    private int currentPositionSearchString;

    private int matchingWords;

    private boolean continueSearch;

    private int distance;

    SearchInternalLevenshtein(@NotNull String[] wordsToSearch, int tolerance, String[] splitPlain) {
        this.wordsToSearch = wordsToSearch;
        this.tolerance = tolerance;
        this.splitPlain = splitPlain;
        continueSearch = true;
    }

    int getCurrentPositionPlainText() {
        return currentPositionPlainText;
    }

    int getCurrentPositionSearchString() {
        return currentPositionSearchString;
    }

    int getMatchingWords() {
        return matchingWords;
    }

    SearchInternalLevenshtein invoke() {
        String currentWordFromPlain = splitPlain[currentPositionPlainText];
        String currentWordToSearch = wordsToSearch[currentPositionSearchString];

        Integer distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain, currentWordToSearch);

        if (distance <= tolerance) {
            matchingWords++;
            currentPositionPlainText++;
            currentPositionSearchString++;
            this.distance += distance;
        } else if (currentPositionSearchString > 0) {
            matchingWords = 0;
            currentPositionSearchString = 0;
            this.distance = 0;
            currentWordToSearch = wordsToSearch[currentPositionSearchString];
            distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain, currentWordToSearch);

            if (distance <= tolerance) {
                matchingWords++;
                currentPositionSearchString++;
            }
            currentPositionPlainText++;
        } else {
            currentPositionPlainText++;
        }
        return this;
    }

    boolean isToSearch() {
        return continueSearch;
    }

    void cancelSearch() {
        continueSearch = false;
    }

    void resetCurrentPositionSearchString() {
        currentPositionSearchString = 0;
    }

    int getDistance() {
        return distance;
    }
}
