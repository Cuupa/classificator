package com.cuupa.classificator.services.kb.semantic.token

class TokenTextPointer(charArray: CharArray, val index: Int) {

    private val charArrayIntern: CharArray = charArray.copyOf(charArray.size)

    operator fun get(i: Int): Char {
        return charArrayIntern[i]
    }

    val charSize: Int
        get() = charArrayIntern.size

}