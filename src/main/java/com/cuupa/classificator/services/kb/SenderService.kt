package com.cuupa.classificator.services.kb

import com.cuupa.classificator.constants.StringConstants
import com.cuupa.classificator.services.kb.semantic.Metadata
import com.cuupa.classificator.services.kb.semantic.SenderToken
import com.cuupa.classificator.services.kb.semantic.token.CountToken

class SenderService(private val senders: List<SenderToken>) {


    fun getSender(text: String): String? {
        val foundSenders = getSenders(text)
        return getMostFittingSender(foundSenders, text)
    }

    private fun getSenders(text: String): List<SenderToken> {
        return senders.filter { it.match(text) }
    }

    private fun getNumberOfOccurrences(foundSenders: List<SenderToken>, text: String): List<SenderToken> {
        foundSenders.forEach { it.countNumberOfOccurences(text) }
        return foundSenders
    }

    private fun getMostFittingSender(foundSenders: List<SenderToken>, text: String): String? {
        val mostFittingSenders = getNumberOfOccurrences(foundSenders, text)
        return mostFittingSenders.maxWithOrNull(compareBy { it.countNumberOfOccurences() })?.name
    }

    fun findSenderFromMetadata(
        metadata: List<Metadata>,
        text: String
    ): String {

        val sendersFromTopic = mutableListOf<Metadata>()
        sendersFromTopic.addAll(metadata.filter { (name) -> StringConstants.sender == name })

        sendersFromTopic.addAll(senders.map { Metadata(StringConstants.sender, it.name) })

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
