package com.cuupa.classificator.engine.services.token

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.services.text.TextSearch

class OneOf : Token() {

    override var distance = 0
        private set

    override fun match(text: String?): Boolean {
        val textSearch = TextSearch(text)
        for (value in tokenValue) {
            val result = textSearch.search(value)
            if (result.contains) {
                distance = result.distance
                return true
            }
        }
        return false
    }

    override fun clone(): OneOf {
        val oneOf = OneOf()
        oneOf.tokenValue = tokenValue.toMutableList()
        return oneOf
    }

    override fun toString() = "OneOf ${super.toString()}"

    companion object {
        const val name = "oneOf"
    }
}