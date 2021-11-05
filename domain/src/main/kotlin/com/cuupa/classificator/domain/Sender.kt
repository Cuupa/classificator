package com.cuupa.classificator.domain

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
class Sender : SemanticResultData() {

    val tokenList = mutableListOf<Token>()
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