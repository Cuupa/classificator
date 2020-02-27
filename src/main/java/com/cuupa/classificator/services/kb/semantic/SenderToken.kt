package com.cuupa.classificator.services.kb.semantic

import com.cuupa.classificator.services.kb.semantic.token.CountToken
import com.cuupa.classificator.services.kb.semantic.token.Token

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class SenderToken {

    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String? = null
    private var numberOfOccurences = 0

    fun addToken(token: Token) {
        tokenList.add(token)
    }

    fun match(text: String?): Boolean {
        for (token in tokenList) {
            if (!token.match(text)) {
                return false
            }
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other is SenderToken) {
            return if (other.name == null && name == null) {
                true
            } else name == other.name
        }
        return false
    }

    fun countNumberOfOccurences(text: String?) {
        numberOfOccurences = CountToken().countOccurences(name, text)
    }

    fun countNumberOfOccurences(): Int {
        return numberOfOccurences
    }

    companion object {
        const val UNKNOWN = "UNKNOWN"
        const val SENDER = "sender"
    }
}