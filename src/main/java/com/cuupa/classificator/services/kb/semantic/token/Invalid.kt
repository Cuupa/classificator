package com.cuupa.classificator.services.kb.semantic.token

class Invalid : Token() {

    override fun match(text: String?): Boolean = false

    override val distance: Int
        get() = Int.MAX_VALUE

    override fun clone(): Token {
        return Invalid().clone()
    }
}
