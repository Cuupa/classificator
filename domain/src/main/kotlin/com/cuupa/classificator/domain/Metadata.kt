package com.cuupa.classificator.domain

data class Metadata(val name: String, val value: String) : SemanticResultData() {
    override fun addToken(token: Token) {
        //noting to do here
    }
}