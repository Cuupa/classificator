package com.cuupa.classificator.engine.services.token

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.services.text.TextSearch

class CountToken : Token() {

    override fun match(text: String?): Boolean = false

    override val distance: Int
        get() = 0

    override fun clone(): CountToken {
        val token = CountToken()
        token.tokenValue = tokenValue.toMutableList()
        return token
    }

    fun countOccurences(textToSearch: String?, text: String?) = TextSearch(text).search(textToSearch).numberOfOccurences
}