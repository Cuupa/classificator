package com.cuupa.classificator.services.kb.semantic.token

import com.cuupa.classificator.services.kb.semantic.text.TextSearch
import java.util.*

class All : Token() {

    override var distance = 0
        private set

    override fun match(text: String?): Boolean {
        val textSearch = TextSearch(text)
        val isMatching = tokenValue.all { textSearch.contains(it) }
        distance = textSearch.distance
        return isMatching
    }

    override fun clone(): Token {
        val token: Token = All()
        token.tokenValue = ArrayList(tokenValue)
        return token
    }

    override fun toString(): String {
        return "ALL " + super.toString()
    }
}