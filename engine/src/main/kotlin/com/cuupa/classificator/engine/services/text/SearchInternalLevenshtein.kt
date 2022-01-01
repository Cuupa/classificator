package com.cuupa.classificator.engine.services.text

import org.apache.commons.text.similarity.LevenshteinDistance

internal class SearchInternalLevenshtein(private val wordsToSearch: SearchText, private val plainText: PlainText,
                                         private val tolerance: Int) {
    private var currentPositionPlainText = 0
        private set
    private var currentPositionSearchString = 0
        private set
    private var matchingWords = 0
        private set
    var isToSearch = true
        private set
    var distance = 0
        private set
    var numberOfOccurences = 0
        private set

    operator fun invoke(): SearchInternalLevenshtein {
        val currentWordFromPlain = plainText[currentPositionPlainText]
        var currentWordToSearch = wordsToSearch[currentPositionSearchString]
        var distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain, currentWordToSearch)
        when {
            matches(distance) -> {
                increment()
                currentPositionPlainText++
                this.distance += distance
            }

            isToReset() -> {
                reset()

                currentWordToSearch = wordsToSearch[currentPositionSearchString]
                distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain, currentWordToSearch)
                if (matches(distance)) {
                    increment()
                }
                currentPositionPlainText++
            }
            else -> currentPositionPlainText++
        }

        if (currentPositionSearchString + 1 > wordsToSearch.length()) {
            numberOfOccurences++
        }

        if (isSearchToCancel(plainText, wordsToSearch)) {
            cancelSearch()
        }
        return this
    }

    private fun increment() {
        matchingWords++
        currentPositionSearchString++
    }

    private fun reset() {
        matchingWords = 0
        currentPositionSearchString = 0
        this.distance = 0
    }

    private fun isToReset() = currentPositionSearchString > 0

    private fun matches(distance: Int) = distance <= tolerance

    fun cancelSearch() {
        isToSearch = false
    }

    private fun isTextEndReached(plaintext: PlainText) = currentPositionPlainText + 1 > plaintext.length()

    private fun isSearchEndReached(wordsToSearch: SearchText) = currentPositionSearchString + 1 > wordsToSearch.length()

    private fun isSearchToCancel(plaintext: PlainText, wordsToSearch: SearchText) =
        isTextEndReached(plaintext) || isSearchEndReached(wordsToSearch)

    fun found() = matchingWords == wordsToSearch.length() && distance <= tolerance
}