package com.cuupa.classificator.services.kb.semantic.token

import com.cuupa.classificator.services.kb.semantic.text.TextSearch

class CountToken : Token() {

    override fun match(text: String?): Boolean {
        return false
    }

    override val distance: Int
        get() = 0

    override fun clone(): Token {
        return CountToken()
    }

    fun countOccurences(textToSearch: String?, text: String?): Int {
        val textSearch = TextSearch(text)
        return textSearch.countOccurence(textToSearch)
    }
}