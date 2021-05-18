package com.cuupa.classificator.knowledgebase.resultobjects

import com.cuupa.classificator.knowledgebase.services.token.CountToken
import com.cuupa.classificator.knowledgebase.services.token.Token
import org.apache.logging.log4j.util.Strings

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class Sender : SemanticResultData() {

    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String = Strings.EMPTY
    private var numberOfOccurrences = 0

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

    fun countNumberOfOccurrences(text: String?) {
        numberOfOccurrences = CountToken().countOccurences(name, text)
    }

    fun countNumberOfOccurrences(): Int {
        return numberOfOccurrences
    }

    override fun hashCode(): Int {
        var result = tokenList.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + numberOfOccurrences
        return result
    }

    companion object {
        const val UNKNOWN = "UNKNOWN"
    }
}