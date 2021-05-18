package com.cuupa.classificator.domain

import org.apache.juli.logging.LogFactory
import org.apache.logging.log4j.util.Strings

class Topic : SemanticResultData() {

    private val log = LogFactory.getLog(Topic::class.java)
    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String = Strings.EMPTY

    override fun addToken(token: Token) {
        tokenList.add(token)
    }

    fun match(text: String): Boolean {
        val match = tokenList.stream().allMatch { it.match(text) }
        if(log.isDebugEnabled) {
            if (match) {
                log.debug("Topic $name matched with ${tokenList.map { it::class.java.simpleName + it.tokenValue }}")
            } else {
                log.debug("Topic $name did not match with ${tokenList.map { it::class.java.simpleName + it.tokenValue }}")
            }
        }
        return match
    }

    companion object {
        const val OTHER = "OTHER"
    }
}