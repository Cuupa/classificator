package com.cuupa.classificator.engine.services.token

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.services.text.TextSearch

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

    override fun clone(): Not {
        val token = Not()
        token.tokenValue = tokenValue.toMutableList()
        return token
    }

    override fun toString() = "NOT ${super.toString()}"

    companion object {
        const val name = "not"
    }
}