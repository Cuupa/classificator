package com.cuupa.classificator.knowledgebase.resultobjects

import com.cuupa.classificator.knowledgebase.services.token.Token

data class Metadata(val name: String, val value: String) : SemanticResultData() {
    override fun addToken(token: Token) {
        //noting to do here
    }
}