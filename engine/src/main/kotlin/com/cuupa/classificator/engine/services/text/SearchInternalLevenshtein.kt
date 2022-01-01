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
        val currentWordToSearch = wordsToSearch[currentPositionSearchString]
        val distance = LevenshteinDistance(tolerance).apply(currentWordFromPlain, currentWordToSearch)
        when {
            // Found a word. increase the indices to check if the following words of the sentence matches
            matches(distance) -> {
                increment()
                currentPositionPlainText++
                this.distance += distance
            }

            // We found a word, but the following word does not match
            // For example looking for "lorem ipsum", but just "lorem" was found
            // Reset the search index
            isToReset() -> {
                reset()
                currentPositionPlainText++
            }
            else -> currentPositionPlainText++
        }

        if (currentPositionSearchString + 1 > wordsToSearch.length()) {
            numberOfOccurences++
            //if(isSearchEndReached(wordsToSearch)) {
            // Reset the position to search the rest of the text.
            // Otherwise, it would just find one occurrence and simply return
            //currentPositionSearchString = 0
            //}
        }

        if (isTextEndReached(plainText) || isSearchEndReached(wordsToSearch)) {
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

    private fun matches(distance: Int) = distance <= tolerance && distance > -1

    fun cancelSearch() {
        isToSearch = false
    }

    private fun isTextEndReached(plaintext: PlainText) = currentPositionPlainText + 1 > plaintext.length()

    private fun isSearchEndReached(wordsToSearch: SearchText) = currentPositionSearchString + 1 > wordsToSearch.length()

    fun found() = matchingWords == wordsToSearch.length() && distance <= tolerance
}