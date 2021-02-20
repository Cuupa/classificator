package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.token.Token

abstract class SemanticResultData {

    abstract fun addToken(token: Token)

}
