package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.token.Token

data class Metadata(val name: String, val value: String) : SemanticResultData() {
    override fun addToken(token: Token) {
        //noting to do here
    }
}