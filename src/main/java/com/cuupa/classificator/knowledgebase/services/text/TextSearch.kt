package com.cuupa.classificator.knowledgebase.services.text

import org.apache.logging.log4j.util.Strings

class TextSearch(plainText: String?) {

    private val plainText: PlainText = PlainText(plainText ?: Strings.EMPTY)

    var distance = 0
        private set

    fun contains(text: String?, tolerance: Int): Boolean {
        val searchText = SearchText(text ?: Strings.EMPTY)
        if (searchText.isEmpty || plainText.isEmpty) {
            return false
        }
        return if (searchText.text == plainText.text) {
            true
        } else search(plainText, searchText, tolerance)
    }

    operator fun contains(text: String?): Boolean {
        return contains(text, 1)
    }

    fun countOccurence(text: String?): Int {
        val searchText = SearchText(text ?: Strings.EMPTY)
        return if (searchText.isEmpty || plainText.isEmpty) {
            0
        } else count(plainText, searchText, 1)
    }

    //TODO: This is still a little bit buggy. Need to redesign this
    private fun count(plainText: PlainText, wordsToSearch: SearchText, tolerance: Int): Int {
        var numberOfOccurences = 0
        var searchInternal = SearchInternalLevenshtein(wordsToSearch, plainText, tolerance)

        while (searchInternal.isToSearch) {
            searchInternal = searchInternal.invoke()
            if (searchInternal.currentPositionSearchString + 1 > wordsToSearch.length()) {
                numberOfOccurences++
                searchInternal.resetCurrentPositionSearchString()
            }
            if (searchInternal.currentPositionPlainText + 1 > plainText.length() || searchInternal.currentPositionSearchString + 1 > wordsToSearch.length()) {
                searchInternal.cancelSearch()
            }
        }
        return numberOfOccurences
    }

    private fun search(tempPlaintext: PlainText, wordsToSearch: SearchText, tolerance: Int): Boolean {
        var searchInternal = SearchInternalLevenshtein(wordsToSearch, tempPlaintext, tolerance)

        while (searchInternal.isToSearch) {
            searchInternal = searchInternal.invoke()
            if (searchInternal.currentPositionPlainText + 1 > tempPlaintext.length() || searchInternal.currentPositionSearchString + 1 > wordsToSearch.length()) {
                searchInternal.cancelSearch()
            }
        }
        distance = searchInternal.distance
        return searchInternal.matchingWords == wordsToSearch.length() && searchInternal.distance <= tolerance
    }
}