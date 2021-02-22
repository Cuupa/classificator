package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.token.CountToken
import com.cuupa.classificator.services.kb.semantic.token.Token
import org.apache.logging.log4j.util.Strings

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class Sender : SemanticResultData() {

    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String = Strings.EMPTY
    private var numberOfOccurences = 0

    override fun addToken(token: Token) {
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
        return when (other) {
            null -> false
            is Sender -> name == other.name
            else -> false
        }
    }

    fun countNumberOfOccurences(text: String?) {
        numberOfOccurences = CountToken().countOccurences(name, text)
    }

    fun countNumberOfOccurences(): Int {
        return numberOfOccurences
    }

    override fun hashCode(): Int {
        var result = tokenList.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + numberOfOccurences
        return result
    }

    companion object {
        const val UNKNOWN = "UNKNOWN"
    }
}