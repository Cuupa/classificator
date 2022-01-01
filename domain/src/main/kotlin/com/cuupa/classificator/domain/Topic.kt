package com.cuupa.classificator.domain

import org.apache.commons.logging.LogFactory

class Topic : SemanticResultData() {

    val tokenList = mutableListOf<Token>()

    override fun addToken(token: Token) {
        tokenList.add(token)
    }

    fun match(text: String): Boolean {
        if (name == "SICKNOTE") {
            println()
        }
        val match = tokenList.stream().allMatch { it.match(text) }
        if (log.isDebugEnabled) {
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
        private val log = LogFactory.getLog(Topic::class.java)
    }
}