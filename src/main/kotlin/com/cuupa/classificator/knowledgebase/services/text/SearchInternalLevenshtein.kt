package com.cuupa.classificator.knowledgebase.services.text

import org.apache.commons.text.similarity.LevenshteinDistance

internal class SearchInternalLevenshtein(private val wordsToSearch: SearchText, private val plainText: PlainText,
                                         private val tolerance: Int) {
    var currentPositionPlainText = 0
        private set
    var currentPositionSearchString = 0
        private set
    var matchingWords = 0
        private set
    var isToSearch = true
        private set
    var distance = 0
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
            else -> {
                currentPositionPlainText++
            }
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

    fun resetCurrentPositionSearchString() {
        currentPositionSearchString = 0
    }
}