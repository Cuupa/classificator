package com.cuupa.classificator.services.kb.semantic.token

import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.dataExtraction.*
import org.apache.commons.lang3.tuple.ImmutablePair
import org.apache.commons.lang3.tuple.Pair
import org.apache.logging.log4j.util.Strings
import java.util.*
import java.util.function.Consumer
import java.util.function.IntPredicate
import java.util.stream.IntStream

class MetaDataToken {

    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String = ""
    private var regexContent: List<Pair<String, String>>? = null

    fun addToken(token: Token) {
        tokenList.add(token)
    }

    fun extract(text: String): List<Metadata> {
        return findMostFittingResult(findMetaData(text, createTempList()))
    }

    private fun findMetaData(text: String, temporaryTokenList: List<Token>): Map<Metadata, Int> {
        val match = HashMap<Metadata, Int>()
        temporaryTokenList.forEach(getTokenConsumer(text, match))
        return match
    }

    private fun getTokenConsumer(text: String, match: MutableMap<Metadata, Int>): Consumer<Token> {
        return Consumer {
            val compiledText: List<List<Pair<String, String>>> = it.tokenValue.map { e: String ->
                compileText(text, e)
            }

            if (compiledText.isEmpty()) {
                return@Consumer
            }

            val tokens = replaceCompiledTextInTokenValue(compiledText, cloneTokens(it, compiledText))
            var searchStream = getIntStream(tokens.size)

            if (searchStream.noneMatch(getPredicateNotTokenMatching(text, it, tokens))) {
                searchStream = getIntStream(tokens.size)
                searchStream.forEach { value: Int ->
                    if (tokens[value].match(text)) {
                        val metadataValue = compiledText[0][value].right
                        addMetadataSynchronized(match, metadataValue, tokens, value)
                    }
                }
            }
        }
    }

    private fun addMetadataSynchronized(match: MutableMap<Metadata, Int>, metadataValue: String, tokens: List<Token>,
                                        value: Int) {
        if (isMetadataAlreadyRegistered(match, metadataValue)) {
            synchronized(MetaDataToken::class.java) {
                if (isMetadataAlreadyRegistered(match, metadataValue)) {
                    val metadata = Metadata(name, metadataValue)
                    match[metadata] = tokens[value].distance
                }
            }
        }
    }

    private fun isMetadataAlreadyRegistered(match: Map<Metadata, Int>, metadataValue: String): Boolean {
        return match.entries.stream().noneMatch { name == it.key.name && it.key.value == metadataValue }
    }

    private fun replaceCompiledTextInTokenValue(compiledText: List<List<Pair<String, String>>>,
                                                tokens: List<Token>): List<Token> {
        IntStream.range(0, compiledText.size).forEach { i: Int ->
            IntStream.range(0, tokens.size).forEach { j: Int ->
                tokens[j].tokenValue[i] = compiledText[i][j].left
            }
        }
        return tokens
    }

    private fun getIntStream(size: Int): IntStream {
        val searchStream = IntStream.range(0, size)
        return if (size > 50) {
            searchStream.parallel()
        } else searchStream
    }

    private fun getPredicateNotTokenMatching(text: String, token: Token, tokens: List<Token>): IntPredicate {
        return IntPredicate { token is Not && tokens[it].match(text) }
    }

    private fun cloneTokens(token: Token, compiledText: List<List<Pair<String, String>>>): List<Token> {
        val tokens: MutableList<Token> = ArrayList()
        IntStream.range(0, compiledText[0].size).forEach { tokens.add(token.clone()) }
        return tokens
    }

    private fun createTempList(): List<Token> {
        return tokenList.map { it.clone() }
    }

    private fun findMostFittingResult(match: Map<Metadata, Int>): List<Metadata> {
        val entries = getMatchesMap(match)
        return entries.entries.minWith(compareBy {
            it.key
        })?.value ?: listOf()
    }

    private fun getMatchesMap(match: Map<Metadata, Int>): Map<Int, MutableList<Metadata>> {
        val entries = mutableMapOf<Int, MutableList<Metadata>>()
        match.forEach { (key: Metadata, value: Int) ->
            if (entries.containsKey(value)) {
                entries[value]!!.add(key)
            } else {
                entries[value] = mutableListOf(key)
            }
        }
        return entries
    }

    private fun compileText(text: String?, tokenValue: String): List<Pair<String, String>> {
        if (text == null || !hasVariable(tokenValue)) {
            return listOf(ImmutablePair(tokenValue, tokenValue))
        }
        val split = tokenValue.split("\\[".toPattern())
        val textBeforeToken = split[0]
        var variable = "[" + split[1]
        val textAfterToken = getTextAfterToken(variable)
        variable = variable.split("]".toPattern())[0] + "]"

        val extract = getExtractForName(variable)
        val matcher = extract.pattern.matcher(text)

        val value: MutableList<Pair<String, String>> = mutableListOf()
        while (matcher.find()) {
            val normalizedValue = extract.normalize(matcher.group())
            value.add(ImmutablePair(textBeforeToken + normalizedValue + textAfterToken, normalizedValue))
        }
        return value
    }

    private fun getExtractForName(name: String): Extract {
        for (pair in regexContent!!) {
            if ("[DATE]" == name && name.contains(pair.left)) {
                return DateExtract(pair.right)
            }
            if ("[IBAN]" == name && name.contains(pair.left)) {
                return IbanExtract(pair.right)
            }
            if ("[SENDER]" == name && name.contains(pair.left)) {
                return SenderExtract(pair.right)
            }
            if (name.contains(pair.left)) {
                return RegexExtract(pair.right)
            }
        }
        throw RuntimeException("There is no extract specified")
    }

    private fun getTextAfterToken(value: String): String {
        val split = value.split("]".toPattern())
        return if (split.size >= 2) {
            split[1]
        } else {
            Strings.EMPTY
        }
    }

    private fun hasVariable(text: String): Boolean {
        return text.contains("[") && text.contains("]")
    }

    fun setRegexContent(regexContent: List<Pair<String, String>>?) {
        this.regexContent = regexContent
    }
}