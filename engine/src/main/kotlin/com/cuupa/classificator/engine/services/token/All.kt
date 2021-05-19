package com.cuupa.classificator.engine.services.token

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.services.text.TextSearch

class All : Token() {

    override var distance = 0
        private set

    override fun match(text: String?): Boolean {
        val textSearch = TextSearch(text)
        val isMatching = tokenValue.all { textSearch.contains(it) }
        distance = textSearch.distance
        return isMatching
    }

    override fun clone() = All().apply { tokenValue = ArrayList(tokenValue) }

    override fun toString() = "ALL ${super.toString()}"

    companion object {
        const val name = "all"
    }
}