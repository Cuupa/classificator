package com.cuupa.classificator.engine.services

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.services.token.*

object Tokens {

    operator fun get(tokenTextPointer: TokenTextPointer): Token {
        return get(findTokenName(tokenTextPointer)).apply { tokenValue = findTokenValue(tokenTextPointer) }
    }

    private fun get(tokenName: String): Token {
        return when (tokenName) {
            OneOf.name -> OneOf()
            Not.name -> Not()
            All.name -> All()
            WildcardBefore.name -> WildcardBefore()
            else -> Invalid()
        }
    }

    private fun findTokenName(pointer: TokenTextPointer): String {
        var tokenName = ""
        for (i in pointer.index - 1 downTo 1) {
            tokenName =
                when {
                    pointer[i] != '{' && pointer[i] != ',' && pointer[i].code > 64 && pointer[i].code < 123 -> {
                        pointer[i].toString() + tokenName
                    }
                    else -> return tokenName
                }
        }
        return tokenName
    }

    private fun findTokenValue(pointer: TokenTextPointer): MutableList<String> {
        val value: MutableList<String> = mutableListOf()
        var tokenValue = StringBuilder()
        for (i in pointer.index + 1 until pointer.charSize) {
            when {
                pointer[i] == ',' -> {
                    value.add(tokenValue.toString())
                    tokenValue = StringBuilder()
                }
                pointer[i] != '"' && pointer[i] != ')' -> {
                    tokenValue.append(pointer[i])
                }
                pointer[i] == ')' -> {
                    value.add(tokenValue.toString())
                    return value
                }
            }
        }
        return value
    }
}