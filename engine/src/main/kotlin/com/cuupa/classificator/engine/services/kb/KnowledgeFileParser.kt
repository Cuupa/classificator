package com.cuupa.classificator.engine.services.kb

import com.cuupa.classificator.domain.SemanticResultData
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.RegexConstants
import com.cuupa.classificator.engine.StringConstants
import com.cuupa.classificator.engine.extensions.Extension.substringBetween
import com.cuupa.classificator.engine.services.Tokens
import com.cuupa.classificator.engine.services.token.InvalidTokenException
import com.cuupa.classificator.engine.services.token.MetaDataToken
import com.cuupa.classificator.engine.services.token.TokenTextPointer
import java.util.stream.IntStream

object KnowledgeFileParser {

    fun parseMetaFile(kbFile: String): MetaDataToken {
        validateToken(kbFile)
        return parseMetaData(kbFile)
    }

    fun parseSenderFile(kbFile: String): Sender {
        validateToken(kbFile)
        return parseSender(kbFile)
    }

    private fun parseSender(kbFile: String) = (fillToken(kbFile, Sender()) as Sender).apply {
        name = kbFile.split(RegexConstants.equalPattern)[0].trim()
    }

    private fun fillToken(kbFile: String, data: SemanticResultData): SemanticResultData {
        val charArray = kbFile.toCharArray()
        for (index in charArray.indices) {
            if (charArray[index] == '(') {
                data.addToken(Tokens[TokenTextPointer(charArray, index)])
            }
            if (charArray[index] == '}') {
                break
            }
        }
        return data
    }

    fun parseRegexFile(filename: String, content: String) = Pair(filename.split(RegexConstants.dotPattern)[0], content)

    private fun parseMetaData(kbFile: String): MetaDataToken {
        return MetaDataToken().apply {
            val charArray = kbFile.toCharArray()
            for (index in charArray.indices) {
                when {
                    charArray[index] == '$' -> name = findExtractName(charArray, index)
                    charArray[index] == '(' && name.isNotEmpty() -> addToken(Tokens[TokenTextPointer(charArray, index)])
                }
            }
        }
    }

    fun parseDatabaseMetadata(metaInfContent: String): KnowledgeBaseMetadata {
        return KnowledgeBaseMetadata().apply {
            version = metaInfContent.substringBetween("version=", "\n")
        }
    }

    fun parseTopicFile(kbFile: String): Topic {
        val split = kbFile.split(RegexConstants.equalPattern)
        val topicName = split[0].trim()
        return (fillToken(kbFile, Topic()) as Topic)
            .apply { name = topicName }
    }

    private fun findExtractName(charArray: CharArray, index: Int): String {
        val extractName = StringBuilder()
        for (i in index until charArray.size) {
            if (charArray[i] == '=') {
                for (j in i - 1 downTo 1) {
                    if (charArray[j] == '$') {
                        return extractName.toString().trim { it <= ' ' }
                    }
                    extractName.insert(0, charArray[j])
                }
            }
        }
        return extractName.toString().trim()
    }

    private fun validateToken(kbFile: String) {
        val charArray = kbFile.toCharArray()
        var curlyOpenBrackets = 0
        var curlyCloseBrackets = 0
        var normalOpenBrackets = 0
        var normalCloseBrackets = 0
        var quotationMarks = 0

        for (c in charArray) {
            when (c) {
                '{' -> curlyOpenBrackets++
                '}' -> curlyCloseBrackets++
                '(' -> normalOpenBrackets++
                ')' -> normalCloseBrackets++
                '"' -> quotationMarks++
            }
        }

        val indexOf = kbFile.indexOf(StringConstants.equal)
        if (indexOf == -1) {
            throw InvalidTokenException("invalid file definition")
        }

        var equalAndBracketValid = false
        IntStream.of(indexOf, charArray.size - 2).forEach { index ->
            var searchIndex = index + 1
            while (!equalAndBracketValid && charArray[searchIndex].isWhitespace()) {
                searchIndex++
            }
            if (!equalAndBracketValid && charArray[searchIndex] == '{') {
                equalAndBracketValid = true
            }
        }

        if (curlyCloseBrackets != curlyOpenBrackets || normalCloseBrackets != normalOpenBrackets) {
            throw InvalidTokenException("invalid bracket count")
        }

        if (quotationMarks % 2 != 0) {
            throw InvalidTokenException("Invalid usage of quotation marks")
        }

        if (!equalAndBracketValid) {
            throw InvalidTokenException("invalid file definition")
        }
    }
}