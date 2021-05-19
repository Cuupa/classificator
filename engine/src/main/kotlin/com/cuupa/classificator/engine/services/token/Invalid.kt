package com.cuupa.classificator.engine.services.token

import com.cuupa.classificator.domain.Token

class Invalid : Token() {

    override fun match(text: String?): Boolean = false

    override val distance: Int
        get() = Int.MAX_VALUE

    override fun clone() = Invalid()
}
