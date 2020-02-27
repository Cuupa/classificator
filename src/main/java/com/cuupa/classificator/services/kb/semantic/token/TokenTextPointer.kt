package com.cuupa.classificator.services.kb.semantic.token

class TokenTextPointer(charArray: CharArray, index: Int) {
    private val charArray: CharArray = charArray.copyOf(charArray.size)
    val index: Int = index

    operator fun get(i: Int): Char {
        return charArray[i]
    }

    val charSize: Int
        get() = charArray.size

}