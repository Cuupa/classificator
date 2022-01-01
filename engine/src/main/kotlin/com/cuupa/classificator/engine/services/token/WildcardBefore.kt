package com.cuupa.classificator.engine.services.token

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.StringConstants
import com.cuupa.classificator.engine.services.text.TextSearch

class WildcardBefore : Token() {

    private val blankRegex = StringConstants.blank.toRegex()

    override var distance = 0
        private set

    override fun match(text: String?): Boolean {
        val textSearch = TextSearch(preProcessText(text))
        for (value in tokenValue) {
            val result = textSearch.search(value)
            if (result.contains) {
                distance = result.distance
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

    override fun clone(): WildcardBefore {
        val token = WildcardBefore()
        token.tokenValue = tokenValue.toMutableList()
        return token
    }

    companion object {
        const val name = "wildcardBefore"
    }
}