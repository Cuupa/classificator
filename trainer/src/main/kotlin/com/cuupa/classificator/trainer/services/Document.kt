package com.cuupa.classificator.trainer.services

import org.apache.commons.lang3.builder.ReflectionToStringBuilder

data class Document(
    var id: String = "",
    var batchName: String = "",
    var content: ByteArray = ByteArray(0),
    var contentType: String = "",
    var plainText: String = "",
    var expectedTopics: List<String> = listOf(),
    var expectedSenders: List<String> = listOf(),
    var expectedMetadata: List<String> = listOf(),
    var actualTopics: List<String> = listOf(),
    var actualSenders: List<String> = listOf(),
    var actualMetadata: List<String> = listOf(),
    var timestamp: Long = -1L,
) {


    override fun toString(): String {
        return ReflectionToStringBuilder.reflectionToString(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (id != other.id) return false
        if (batchName != other.batchName) return false
        if (!content.contentEquals(other.content)) return false
        if (contentType != other.contentType) return false
        if (plainText != other.plainText) return false
        if (expectedTopics != other.expectedTopics) return false
        if (expectedSenders != other.expectedSenders) return false
        if (expectedMetadata != other.expectedMetadata) return false
        if (actualTopics != other.actualTopics) return false
        if (actualSenders != other.actualSenders) return false
        if (actualMetadata != other.actualMetadata) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + batchName.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + plainText.hashCode()
        result = 31 * result + expectedTopics.hashCode()
        result = 31 * result + expectedSenders.hashCode()
        result = 31 * result + expectedMetadata.hashCode()
        result = 31 * result + actualTopics.hashCode()
        result = 31 * result + actualSenders.hashCode()
        result = 31 * result + actualMetadata.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
