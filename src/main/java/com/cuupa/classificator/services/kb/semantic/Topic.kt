package com.cuupa.classificator.services.kb.semantic

import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken
import com.cuupa.classificator.services.kb.semantic.token.Token

class Topic {

    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String? = null
    private val metadata: MutableList<Metadata> = mutableListOf()
    private val metaDataToken: MutableList<MetaDataToken> = mutableListOf()

    fun addToken(token: Token) {
        tokenList.add(token)
    }

    fun match(text: String): Boolean {
        return tokenList.stream()
                .allMatch { token: Token -> token.match(text) }
    }

    fun addMetaData(metadata: MetaDataToken) {
        metaDataToken.add(metadata)
    }

    fun getMetaData(text: String): MutableList<Metadata> {
        if (metadata.isEmpty()) {
            metaDataToken
                    .map { data: MetaDataToken -> data.extract(text) }
                    .forEach { collection: List<Metadata> -> metadata.addAll(collection) }
        }
        return metadata
    }

    val metaDataList: List<MetaDataToken>
        get() = metaDataToken

    fun addMetaDataList(metaDataTokenList: List<MetaDataToken>) {
        metaDataToken.addAll(metaDataTokenList)
    }

    companion object {
        const val OTHER = "OTHER"
    }
}