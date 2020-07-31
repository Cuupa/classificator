package com.cuupa.classificator.services.kb.semantic.text

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
            distance <= tolerance -> {
                matchingWords++
                currentPositionPlainText++
                currentPositionSearchString++
                this.distance += distance
            }
            currentPositionSearchString > 0 -> {
                matchingWords = 0
                currentPositionSearchString = 0
                this.distance = 0
                currentWordToSearch = wordsToSearch[currentPositionSearchString]
                distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain, currentWordToSearch)
                if (distance <= tolerance) {
                    matchingWords++
                    currentPositionSearchString++
                }
                currentPositionPlainText++
            }
            else -> {
                currentPositionPlainText++
            }
        }
        return this
    }

    fun cancelSearch() {
        isToSearch = false
    }

    fun resetCurrentPositionSearchString() {
        currentPositionSearchString = 0
    }

}