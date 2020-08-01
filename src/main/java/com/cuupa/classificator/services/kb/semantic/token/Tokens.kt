package com.cuupa.classificator.services.kb.semantic.token

import org.apache.logging.log4j.util.Strings

object Tokens {

    operator fun get(tokenTextPointer: TokenTextPointer): Token {
        val token = get(findTokenName(tokenTextPointer))
        token.tokenValue = findTokenValue(tokenTextPointer)
        return token
    }

    private operator fun get(tokenName: String): Token {
        return when (tokenName) {
            OneOf.name -> OneOf()
            Not.name -> Not()
            All.name -> All()
            WildcardBefore.name -> WildcardBefore()
            else -> Invalid()
        }
    }

    private fun findTokenName(pointer: TokenTextPointer): String {
        var tokenName = Strings.EMPTY
        for (i in pointer.index - 1 downTo 1) {
            tokenName = if (pointer[i] != '{' && pointer[i] != ',' && pointer[i].toInt() > 64 && pointer[i].toInt() < 123) {
                pointer[i].toString() + tokenName
            } else {
                return tokenName
            }
        }
        return tokenName
    }

    private fun findTokenValue(pointer: TokenTextPointer): MutableList<String> {
        val value: MutableList<String> = mutableListOf()
        var tokenValue = StringBuilder()
        for (i in pointer.index + 1 until pointer.charSize) {
            if (pointer[i] == ',') {
                value.add(tokenValue.toString())
                tokenValue = StringBuilder()
            } else if (pointer[i] != '"' && pointer[i] != ')') {
                tokenValue.append(pointer[i])
            } else if (pointer[i] == ')') {
                value.add(tokenValue.toString())
                return value
            }
        }
        return value
    }
}