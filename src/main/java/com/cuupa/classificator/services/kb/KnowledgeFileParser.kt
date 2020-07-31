package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.Topic
import com.cuupa.classificator.services.kb.semantic.token.InvalidTokenException
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken
import com.cuupa.classificator.services.kb.semantic.token.TokenTextPointer
import com.cuupa.classificator.services.kb.semantic.token.Tokens
import org.apache.commons.lang3.tuple.Pair

object KnowledgeFileParser {

    val equalPattern = "=".toPattern()

    fun parseTopicFile(kbFile: String): Topic {
        validateToken(kbFile)
        return parseTopic(kbFile)
    }

    fun parseMetaFile(kbFile: String): MetaDataToken {
        validateToken(kbFile)
        return parseMetaData(kbFile)
    }

    fun parseSenderFile(kbFile: String): SenderToken {
        validateToken(kbFile)
        return parseSender(kbFile)
    }

    private fun parseSender(kbFile: String): SenderToken {
        val senderToken = fillToken(kbFile)
        senderToken.name = kbFile.split(equalPattern)[0].trim()
        return senderToken
    }

    private fun fillToken(kbFile: String): SenderToken {
        val token = SenderToken()
        val charArray = kbFile.toCharArray()
        for (index in charArray.indices) {
            if (charArray[index] == '(') {
                token.addToken(Tokens[TokenTextPointer(charArray, index)])
            }
            if (charArray[index] == '}') {
                break
            }
        }
        return token
    }

    fun parseRegexFile(filename: String, content: String): Pair<String, String> {
        return Pair.of(filename.split("\\.".toPattern())[0], content)
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

    @JvmStatic
    fun parseTopic(kbFile: String): Topic {
        val split = kbFile.split("=".toPattern())
        val topicName = split[0].trim { it <= ' ' }
        val topic = Topic()
        topic.name = topicName
        val charArray = kbFile.toCharArray()
        for (index in charArray.indices) {
            if (charArray[index] == '(') {
                topic.addToken(Tokens[TokenTextPointer(charArray, index)])
            }
            if (charArray[index] == '}') {
                break
            }
        }
        // these are the metadata which is only applicable for the specific topic
        if (kbFile.contains("$")) {
            var metadata = MetaDataToken()
            for (index in charArray.indices) {
                if (charArray[index] == '$') {
                    metadata.name = findExtractName(charArray, index)
                } else if (charArray[index] == '(' && metadata.name.isNotEmpty()) {
                    metadata.addToken(Tokens[TokenTextPointer(charArray, index)])
                    topic.addMetaData(metadata)
                    metadata = MetaDataToken()
                }
            }
        }
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
        return extractName.toString().trim { it <= ' ' }
    }

    private fun validateToken(kbFile: String) {
        val charArray = kbFile.toCharArray()
        var curlyOpenBrackets = 0
        var curlyCloseBrackets = 0
        var normalOpenBrackets = 0
        var normalCloseBrackets = 0
        for (c in charArray) {
            when (c) {
                '{' -> {
                    curlyOpenBrackets++
                }
                '}' -> {
                    curlyCloseBrackets++
                }
                '(' -> {
                    normalOpenBrackets++
                }
                ')' -> {
                    normalCloseBrackets++
                }
            }
        }
        if (curlyCloseBrackets != curlyOpenBrackets || normalCloseBrackets != normalOpenBrackets) {
            throw InvalidTokenException("invalid bracket count")
        }
        if (!kbFile.contains("=")) {
            throw InvalidTokenException("invalid file definition")
        }
    }
}