package com.cuupa.classificator.knowledgebase.services.token

class Invalid : Token() {

    override fun match(text: String?): Boolean = false

    override val distance: Int
        get() = Int.MAX_VALUE

    override fun clone(): Token {
        return Invalid().clone()
    }
}
