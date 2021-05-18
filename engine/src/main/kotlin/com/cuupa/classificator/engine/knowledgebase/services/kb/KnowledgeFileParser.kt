package com.cuupa.classificator.engine.knowledgebase.services.kb

import com.cuupa.classificator.domain.SemanticResultData
import com.cuupa.classificator.domain.Sender
import com.cuupa.classificator.domain.Topic
import com.cuupa.classificator.engine.RegexConstants
import com.cuupa.classificator.engine.StringConstants
import com.cuupa.classificator.engine.knowledgebase.services.Tokens
import com.cuupa.classificator.engine.knowledgebase.services.token.InvalidTokenException
import com.cuupa.classificator.engine.knowledgebase.services.token.MetaDataToken
import com.cuupa.classificator.engine.knowledgebase.services.token.TokenTextPointer
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

    private fun parseSender(kbFile: String): Sender {
        val senderToken = fillToken(kbFile, Sender()) as Sender
        senderToken.name = kbFile.split(RegexConstants.equalPattern)[0].trim()
        return senderToken
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

    fun parseRegexFile(filename: String, content: String): Pair<String, String> {
        return Pair(filename.split(RegexConstants.dotPattern)[0], content)
    }

    private fun parseMetaData(kbFile: String): MetaDataToken {
        val metadata = MetaDataToken()
        val charArray = kbFile.toCharArray()
        for (index in charArray.indices) {
            if (charArray[index] == '$') {
                metadata.name = findExtractName(charArray, index)
            } else if (charArray[index] == '(' && metadata.name.isNotEmpty()) {
                metadata.addToken(Tokens[TokenTextPointer(charArray, index)])
            }
        }
        return metadata
    }

    fun parseDatabaseMetadata(metaInfContent: String): KnowledgeBaseMetadata {
        val knowledgeBaseMetadata = KnowledgeBaseMetadata()
        knowledgeBaseMetadata.version = metaInfContent.substringAfter("version=").substringBefore("\n")
        return knowledgeBaseMetadata
    }

    fun parseTopicFile(kbFile: String): Topic {
        val split = kbFile.split(RegexConstants.equalPattern)
        val topicName = split[0].trim()
        return(fillToken(kbFile, Topic()) as Topic)
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