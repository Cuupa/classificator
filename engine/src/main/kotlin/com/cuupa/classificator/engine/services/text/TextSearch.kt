package com.cuupa.classificator.engine.services.text


class TextSearch(plainText: String?) {

    private val plainText: PlainText = PlainText(plainText ?: "")

    fun search(text: String?): SearchResult {
        return search(text, 1)
    }

    fun search(text: String?, tolerance: Int): SearchResult {
        val searchText = SearchText(text ?: "")
        if (searchText.isEmpty() || plainText.isEmpty()) {
            return SearchResult()
        }
        return search(plainText, searchText, tolerance)
    }

    private fun search(plaintext: PlainText, wordsToSearch: SearchText, tolerance: Int): SearchResult {
        var search = SearchInternalLevenshtein(wordsToSearch, plaintext, tolerance)

        while (search.isToSearch) {
            search = search.invoke()
        }
        return SearchResult(search.found, search.distance, search.numberOfOccurrences)
    }
}