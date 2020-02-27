package com.cuupa.classificator.services.kb.semantic.text

class TextSearch(plainText: String?) {

    private val plainText: PlainText = PlainText(plainText ?: "")
    var distance = 0
        private set

    fun contains(text: String?, tolerance: Int): Boolean {
        return try {
            val searchText = SearchText(text ?: "")
            if (searchText.isEmpty || plainText.isEmpty) {
                return false
            }
            if (searchText.text == plainText.text) {
                true
            } else search(plainText, searchText, tolerance)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    operator fun contains(text: String?): Boolean {
        return contains(text, 1)
    }

    fun countOccurence(text: String?): Int {
        val searchText = SearchText(text ?: "")
        return if (searchText.isEmpty || plainText.isEmpty) {
            0
        } else count(plainText, searchText, 1)
    }

    private fun count(plainText: PlainText, wordsToSearch: SearchText,
                      tolerance: Int): Int {
        var numberOfOccurences = 0
        var searchInternal = SearchInternalLevenshtein(wordsToSearch, plainText, tolerance)
        while (searchInternal.isToSearch) {
            searchInternal = searchInternal.invoke()
            if (searchInternal.currentPositionSearchString + 1 > wordsToSearch.length()) {
                numberOfOccurences++
                searchInternal.resetCurrentPositionSearchString()
            }
            if (searchInternal.currentPositionPlainText + 1 > plainText.length() || searchInternal.currentPositionSearchString + 1 > wordsToSearch
                            .length()) {
                searchInternal.cancelSearch()
            }
        }
        return numberOfOccurences
    }

    private fun search(tempPlaintext: PlainText,
                       wordsToSearch: SearchText, tolerance: Int): Boolean {
        var searchInternal = SearchInternalLevenshtein(wordsToSearch,
                tempPlaintext,
                tolerance)
        while (searchInternal.isToSearch) {
            searchInternal = searchInternal.invoke()
            if (searchInternal.currentPositionPlainText + 1 > tempPlaintext.length() || searchInternal.currentPositionSearchString + 1 > wordsToSearch
                            .length()) {
                searchInternal.cancelSearch()
            }
        }
        distance = searchInternal.distance
        return searchInternal.matchingWords == wordsToSearch.length() && searchInternal.distance <= tolerance
    }

    companion object {
        private const val BLANK = " "
    }

}