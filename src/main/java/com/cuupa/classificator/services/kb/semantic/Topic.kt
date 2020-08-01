package com.cuupa.classificator.services.kb.semantic

import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken
import com.cuupa.classificator.services.kb.semantic.token.Token
import org.apache.logging.log4j.util.Strings

class Topic {

    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String = Strings.EMPTY
    private val metadata: MutableList<Metadata> = mutableListOf()
    private val metaDataToken: MutableList<MetaDataToken> = mutableListOf()

    fun addToken(token: Token) {
        tokenList.add(token)
    }

    fun match(text: String): Boolean {
        return tokenList.stream().allMatch { it.match(text) }
    }

    fun addMetaData(metadata: MetaDataToken) {
        metaDataToken.add(metadata)
    }

    fun getMetaData(text: String): MutableList<Metadata> {
        metadata.clear()
        metaDataToken.map { it.extract(text) }.forEach { metadata.addAll(it) }
        return metadata
    }

    val metaDataList: List<MetaDataToken>
        get() = metaDataToken.toList()

    fun addMetaDataList(metaDataTokenList: List<MetaDataToken>) {
        metaDataToken.addAll(metaDataTokenList)
    }

    companion object {
        const val OTHER = "OTHER"
    }
}