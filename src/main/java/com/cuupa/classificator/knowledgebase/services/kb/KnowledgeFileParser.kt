package com.cuupa.classificator.knowledgebase.services.kb

import com.cuupa.classificator.constants.RegexConstants
import com.cuupa.classificator.constants.StringConstants
import com.cuupa.classificator.knowledgebase.resultobjects.SemanticResultData
import com.cuupa.classificator.knowledgebase.resultobjects.Sender
import com.cuupa.classificator.knowledgebase.resultobjects.Topic
import com.cuupa.classificator.knowledgebase.services.Tokens
import com.cuupa.classificator.knowledgebase.services.token.InvalidTokenException
import com.cuupa.classificator.knowledgebase.services.token.MetaDataToken
import com.cuupa.classificator.knowledgebase.services.token.TokenTextPointer
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
        val topic = fillToken(kbFile, Topic()) as Topic
        topic.name = topicName
        return topic
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
        for (c in charArray) {
            when (c) {
                '{' -> curlyOpenBrackets++
                '}' -> curlyCloseBrackets++
                '(' -> normalOpenBrackets++
                ')' -> normalCloseBrackets++

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
        if (!equalAndBracketValid) {
            println(kbFile)
            throw InvalidTokenException("invalid file definition")
        }
    }
}