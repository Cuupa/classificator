package com.cuupa.classificator.services.kb

import com.cuupa.classificator.constants.StringConstants
import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.token.CountToken
import org.apache.juli.logging.LogFactory

class SenderService {

    private val LOG = LogFactory.getLog(SenderService::class.java)

    fun getSender(senders: List<SenderToken>, text: String): String? {
        val sender = getSenders(senders, text)
        return getMostFittingSender(sender, text)
    }

    private fun getSenders(senders: List<SenderToken>, text: String): List<SenderToken> {
        return senders.filter { it.match(text) }
    }

    private fun getNumberOfOccurrences(senders: List<SenderToken>, text: String): List<SenderToken> {
        senders.forEach { it.countNumberOfOccurences(text) }
        return senders
    }

    fun getMostFittingSender(senders: List<SenderToken>, text: String): String? {
        val mostFittingSenders = getNumberOfOccurrences(senders, text)
        return mostFittingSenders.maxWithOrNull(compareBy { it.countNumberOfOccurences() })?.name
    }

    fun findSenderFromMetadata(
        semanticResults: List<SemanticResult>,
        senderTokens: List<SenderToken>,
        text: String
    ): String? {

        val sendersFromTopic = mutableListOf<Metadata>()
        for ((_, _, metaData) in semanticResults) {
            sendersFromTopic.addAll(metaData.filter { (name) -> StringConstants.sender == name })
        }

        sendersFromTopic.addAll(senderTokens.map { Metadata(StringConstants.sender, it.name) })

        val filteredText = sendersFromTopic.filter { text.contains(it.value) }
        val mutableMapOf = mutableMapOf<String, Int>()
        filteredText.forEach(weightSenders(mutableMapOf, text))
        return mutableMapOf.filter(lessOrEqualFiveSpacesInWord()).maxByOrNull { it.value }?.key ?: SenderToken.UNKNOWN
    }

    private fun weightSenders(mutableMapOf: MutableMap<String, Int>, text: String): (Metadata) -> Unit = {
        mutableMapOf[it.value] = CountToken().countOccurences(it.value, text) * it.value.length
    }

    /**
     * If there are more then 5 spaces, we can assume we missinterpreted a sentence as a sender
     */
    private fun lessOrEqualFiveSpacesInWord(): (Map.Entry<String, Int>) -> Boolean =
        { it.key.count { char -> ' ' == char } <= 5 }
}
