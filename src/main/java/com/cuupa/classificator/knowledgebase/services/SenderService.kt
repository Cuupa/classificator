package com.cuupa.classificator.knowledgebase.services

import com.cuupa.classificator.constants.StringConstants
import com.cuupa.classificator.knowledgebase.resultobjects.Metadata
import com.cuupa.classificator.knowledgebase.resultobjects.Sender
import com.cuupa.classificator.knowledgebase.services.token.CountToken

class SenderService(private val senders: List<Sender>) {

    fun getSender(text: String): String? {
        val foundSenders = getSenders(text)
        return getMostFittingSender(foundSenders, text)
    }

    private fun getSenders(text: String): List<Sender> {
        return senders.filter { it.match(text) }
    }

    private fun getNumberOfOccurrences(foundSenders: List<Sender>, text: String): List<Sender> {
        foundSenders.forEach { it.countNumberOfOccurrences(text) }
        return foundSenders
    }

    private fun getMostFittingSender(foundSenders: List<Sender>, text: String): String? {
        val mostFittingSenders = getNumberOfOccurrences(foundSenders, text)
        return mostFittingSenders.maxWithOrNull(compareBy { it.countNumberOfOccurrences() })?.name
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
        return mutableMapOf.filter(lessOrEqualFiveSpacesInWord()).maxByOrNull { it.value }?.key ?: Sender.UNKNOWN
    }

    private fun weightSenders(mutableMapOf: MutableMap<String, Int>, text: String): (Metadata) -> Unit = {
        mutableMapOf[it.value] = CountToken().countOccurences(it.value, text) * it.value.length
    }

    /**
     * If there are more then 5 spaces, we can assume we misinterpreted a sentence as a sender
     */
    private fun lessOrEqualFiveSpacesInWord(): (Map.Entry<String, Int>) -> Boolean =
        { it.key.count { char -> ' ' == char } <= 5 }
}
