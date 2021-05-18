package com.cuupa.classificator.knowledgebase.services.token

import com.cuupa.classificator.knowledgebase.services.text.TextSearch
import java.util.*

class Not : Token() {

    override fun match(text: String?): Boolean {
        val plaintext = TextSearch(text)
        for (token in tokenValue) {
            if (plaintext.contains(token)) {
                return false
            }
        }
        return true
    }

    override val distance: Int
        get() = 0

    override fun clone(): Token {
        val token = Not()
        token.tokenValue = ArrayList(tokenValue)
        return token
    }

    override fun toString(): String {
        return "NOT " + super.toString()
    }

    companion object {
        const val name = "not"
    }
}