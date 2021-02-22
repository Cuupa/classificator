package com.cuupa.classificator.services.kb.semantic.token

import com.cuupa.classificator.constants.RegexConstants
import com.cuupa.classificator.constants.StringConstants
import com.cuupa.classificator.services.kb.semantic.text.TextSearch
import org.apache.logging.log4j.util.Strings
import java.util.*

class WildcardBefore : Token() {

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

        val words = text.replace(StringConstants.newLine, Strings.EMPTY)
            .replace(StringConstants.carriageReturn, Strings.EMPTY)
            .split(RegexConstants.emptyStringRegex)

        val stringBuilder = StringBuilder()
        tokenValue.forEach { token ->
            words.forEach { word ->
                var processWord = word
                while (processWord.length > token.length) {
                    processWord = processWord.substring(1)
                }
                stringBuilder.append(processWord)
                stringBuilder.append(Strings.EMPTY)
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