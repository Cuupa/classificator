package com.cuupa.classificator.knowledgebase.services.token

abstract class Token {

    var tokenValue: MutableList<String> = mutableListOf()

    abstract fun match(text: String?): Boolean

    abstract val distance: Int

    abstract fun clone(): Token

    override fun toString(): String {
        return tokenValue.joinToString(",", "", "")
    }
}