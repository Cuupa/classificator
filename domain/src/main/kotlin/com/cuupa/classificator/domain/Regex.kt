package com.cuupa.classificator.domain

class Regex : SemanticResultData() {

    var regexContent = listOf<Pair<String, String>>()

    override fun addToken(token: Token) {
        //nothing to do here
    }

}
