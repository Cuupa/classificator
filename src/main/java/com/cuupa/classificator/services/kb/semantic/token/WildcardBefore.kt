package com.cuupa.classificator.services.kb.semantic.token

import com.cuupa.classificator.constants.StringConstants
import com.cuupa.classificator.services.kb.semantic.text.TextSearch
import java.util.*

class WildcardBefore : Token() {

    private val blankRegex = StringConstants.blank.toRegex()

    override var distance = 0
        private set

    override fun match(text: String?): Boolean {
        val textSearch = TextSearch(preProcessText(text))
        for (value in tokenValue) {
            if (textSearch.contains(value)) {
                distance = textSearch.distance
                return true
            }
        }
        return false
    }

    private fun preProcessText(text: String?): String? {
        if (text.isNullOrEmpty()) {
            return text
        }

        val words = text.replace(StringConstants.newLine, StringConstants.blank)
            .replace(StringConstants.carriageReturn, StringConstants.blank)
            .split(blankRegex)

        val stringBuilder = StringBuilder()
        tokenValue.forEach { token ->
            words.forEach { word ->
                var processWord = word
                while (processWord.length > token.length) {
                    processWord = processWord.substring(1)
                }
                stringBuilder.append(processWord)
                stringBuilder.append(StringConstants.blank)
            }

        }
        return stringBuilder.toString()
    }

    override fun clone(): Token {
        val token: Token = WildcardBefore()
        token.tokenValue = ArrayList(tokenValue)
        return token
    }

    companion object {
        const val name = "wildcardBefore"
    }
}