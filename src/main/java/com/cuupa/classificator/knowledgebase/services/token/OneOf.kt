package com.cuupa.classificator.knowledgebase.services.token

import com.cuupa.classificator.knowledgebase.services.text.TextSearch
import java.util.*

class OneOf : Token() {

    override var distance = 0
        private set

    override fun match(text: String?): Boolean {
        val textSearch = TextSearch(text)
        for (value in tokenValue) {
            if (textSearch.contains(value)) {
                distance = textSearch.distance
                return true
            }
        }
        return false
    }

    override fun clone(): Token {
        val token: Token = OneOf()
        token.tokenValue = ArrayList(tokenValue)
        return token
    }

    override fun toString(): String {
        return "OneOf " + super.toString()
    }

    companion object {
        const val name = "oneOf"
    }
}