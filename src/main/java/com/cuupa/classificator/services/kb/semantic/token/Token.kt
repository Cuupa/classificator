package com.cuupa.classificator.services.kb.semantic.token

abstract class Token {
    @JvmField
    var tokenValue: MutableList<String> = mutableListOf()

    abstract fun match(text: String?): Boolean

    abstract val distance: Int
    abstract fun clone(): Token
    override fun toString(): String {
        return java.lang.String.join(",", tokenValue)
    }
}