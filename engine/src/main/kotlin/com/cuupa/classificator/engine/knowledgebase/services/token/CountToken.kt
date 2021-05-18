package com.cuupa.classificator.engine.knowledgebase.services.token

import com.cuupa.classificator.engine.knowledgebase.services.text.TextSearch
import com.cuupa.classificator.domain.Token

class CountToken : Token() {

    override fun match(text: String?): Boolean = false

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