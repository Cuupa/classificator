package com.cuupa.classificator.domain

class Metadata : SemanticResultData() {

    lateinit var value: String

    var tokenList = mutableListOf<Token>()
    var regexContent: List<Pair<String, String>> = listOf()

    override fun addToken(token: Token) {
        tokenList.add(token)
    }
}