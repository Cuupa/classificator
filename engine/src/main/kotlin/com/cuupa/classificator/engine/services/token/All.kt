package com.cuupa.classificator.engine.services.token

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.services.text.TextSearch

class All : Token() {

    override var distance = 0
        private set

    override fun match(text: String?): Boolean {
        val textSearch = TextSearch(text)
        val isMatching = tokenValue.all {
            val result = textSearch.search(it)
            distance = result.distance
            result.contains
        }
        return isMatching
    }

    override fun clone(): All {
        val token = All()
        token.tokenValue = tokenValue.toMutableList()
        return token
    }

    override fun toString() = "ALL ${super.toString()}"

    companion object {
        const val name = "all"
    }
}