package com.cuupa.classificator.services.kb.semantic

import com.cuupa.classificator.services.kb.semantic.token.Token
import org.apache.juli.logging.LogFactory
import org.apache.logging.log4j.util.Strings

class Topic {

    private val log = LogFactory.getLog(Topic::class.java)
    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String = Strings.EMPTY

    fun addToken(token: Token) {
        tokenList.add(token)
    }

    fun match(text: String): Boolean {
        val match = tokenList.stream().allMatch { it.match(text) }
        if (match) {
            log.info("Topic $name matched with ${tokenList.map { it::class.java.simpleName + it.tokenValue }}")
        } else {
            log.info("Topic $name did not match with ${tokenList.map { it::class.java.simpleName + it.tokenValue }}")
        }
        return match
    }

    companion object {
        const val OTHER = "OTHER"
    }
}