package com.cuupa.classificator.engine.knowledgebase.services.token

import com.cuupa.classificator.domain.Token
import com.cuupa.classificator.engine.RegexConstants
import com.cuupa.classificator.engine.knowledgebase.services.dataExtraction.*
import org.apache.juli.logging.LogFactory
import org.apache.logging.log4j.util.Strings
import java.util.*
import java.util.function.Consumer
import java.util.stream.IntStream
import com.cuupa.classificator.domain.Metadata

class MetaDataToken {

    private val tokenList: MutableList<Token> = mutableListOf()
    var name: String = ""
    var regexContent: List<Pair<String, String>> = listOf()

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
        return Consumer { token ->
            val compiledText = token.tokenValue.map {
                tryCompile(text, it)
            }

            if (compiledText.isEmpty()) {
                return@Consumer
            }

            val tokens = replaceCompiledTextInTokenValue(compiledText, cloneTokens(token, compiledText))
            var searchStream = getIntStream(tokens.size)

            val parallel = searchStream.isParallel
            searchStream.forEach {
                if (tokens[it] !is Not && tokens[it].match(text)) {
                    val metadataValue = compiledText[0][it].second
                    if (parallel) {
                        addMetadataSynchronized(match, metadataValue, tokens, it)
                    } else {
                        addMetadata(match, metadataValue, tokens, it)
                    }
                }
            }
            searchStream = getIntStream(tokens.size)
            searchStream.forEach {
                if (tokens[it] is Not && !tokens[it].match(text)) {
                    // We want the reversed result, because we want to know if the text contains this match
                    val excluded = match.filter { metadata -> metadata.key.value == compiledText[0][it].second }
                    if (match.isNotEmpty() && excluded.isNotEmpty()) {
                        match.remove(excluded.keys.first())
                    }
                }
            }
        }
    }

    private fun tryCompile(
        text: String,
        e: String
    ) = try {
        compileText(text, e)
    } catch (re: RuntimeException) {
        log.error(re)
        listOf()
    }

    private fun addMetadata(
        match: MutableMap<Metadata, Int>, metadataValue: String, tokens: List<Token>,
        value: Int
    ) {
        if (isMetadataAlreadyRegistered(match, metadataValue)) {
            val metadata = Metadata(name, metadataValue)
            match[metadata] = tokens[value].distance
        }
    }

    private fun addMetadataSynchronized(
        match: MutableMap<Metadata, Int>, metadataValue: String, tokens: List<Token>,
        value: Int
    ) {
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

    private fun replaceCompiledTextInTokenValue(
        compiledText: List<List<Pair<String, String>>>,
        tokens: List<Token>
    ): List<Token> {
        IntStream.range(0, compiledText.size).forEach { i: Int ->
            IntStream.range(0, tokens.size).forEach { j: Int ->
                tokens[j].tokenValue[i] = compiledText[i][j].first
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

    private fun cloneTokens(token: Token, compiledText: List<List<Pair<String, String>>>): List<Token> {
        val tokens = mutableListOf<Token>()
        IntStream.range(0, compiledText[0].size).forEach { tokens.add(token.clone()) }
        return tokens
    }

    private fun createTempList(): List<Token> = tokenList.map { it.clone() }

    private fun findMostFittingResult(match: Map<Metadata, Int>): List<Metadata> {
        val entries = getMatchesMap(match)
        return entries.entries.minWithOrNull(compareBy {
            it.key
        })?.value ?: listOf()
    }

    private fun getMatchesMap(match: Map<Metadata, Int>): Map<Int, MutableList<Metadata>> {
        val entries = mutableMapOf<Int, MutableList<Metadata>>()
        match.forEach { (key: Metadata, value: Int) ->
            if (entries.containsKey(value)) {
                entries[value]?.add(key)
            } else {
                entries[value] = mutableListOf(key)
            }
        }
        return entries
    }

    private fun compileText(text: String?, tokenValue: String): List<Pair<String, String>> {
        if (text == null || !hasVariable(tokenValue)) {
            return listOf(Pair(tokenValue, tokenValue))
        }
        val split = tokenValue.split(RegexConstants.squareBracketOpenPattern)
        val textBeforeToken = split[0]
        var variable = "[" + split[1]
        val textAfterToken = getTextAfterToken(variable)
        variable = variable.split(RegexConstants.squareBracketClosePattern)[0] + "]"

        val extract = getExtractForName(variable)
        return extract.get(text, textBeforeToken, textAfterToken)
    }

    private fun getExtractForName(name: String): Extract {
        for (pair in regexContent) {
            when {
                isDateExtract(name, pair) -> return DateExtract(pair.second)
                isIbanExtract(name, pair) -> return IbanExtract(pair.second)
                isSenderExtract(name, pair) -> return SenderExtract(pair.second)
                isRegexExtract(name, pair) -> return RegexExtract(pair.second)
                isPhoneNumberExtract(name, pair) -> return PhoneNumberExtract(pair.second)
                isTimespanExtract(name, pair) -> return TimespanExtract(pair.second)
            }
        }
        throw RuntimeException("There is no extract for $name specified")
    }

    private fun isTimespanExtract(name: String, pair: Pair<String, String>) =
        TimespanExtract.name == name && DateExtract.name.contains(pair.first)

    private fun isPhoneNumberExtract(name: String, pair: Pair<String, String>) =
        PhoneNumberExtract.name == name && name.contains(pair.first)

    private fun isRegexExtract(name: String, pair: Pair<String, String>) = name.contains(pair.first)

    private fun isSenderExtract(name: String, pair: Pair<String, String>) = SenderExtract.name == name && name.contains(
        pair.first
    )

    private fun isIbanExtract(
        name: String,
        pair: Pair<String, String>
    ) = IbanExtract.name == name && name.contains(pair.first)

    private fun isDateExtract(
        name: String,
        pair: Pair<String, String>
    ) = DateExtract.name == name && name.contains(pair.first)

    private fun getTextAfterToken(value: String): String {
        val split = value.split("]".toPattern())
        return when (split.size) {
            in 2..Int.MAX_VALUE -> split[1]
            else -> Strings.EMPTY
        }
    }

    private fun hasVariable(text: String): Boolean {
        return text.contains("[") && text.contains("]")
    }

    override fun toString(): String {
        return name
    }

    companion object {
        private val log = LogFactory.getLog(MetaDataToken::class.java)
    }
}