package com.cuupa.classificator.knowledgebase.resultobjects

import com.cuupa.classificator.knowledgebase.services.token.Token

abstract class SemanticResultData {

    abstract fun addToken(token: Token)

}
