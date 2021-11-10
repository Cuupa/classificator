package com.cuupa.classificator.trainer.services

import org.apache.commons.lang3.builder.ReflectionToStringBuilder

data class Document(
    var id: String? = null,
    var batchName: String? = null,
    var content: ByteArray? = null,
    var contentType: String? = null,
    var plainText: String? = null,
    var topics: List<String> = listOf(),
    var senders: List<String> = listOf(),
    var metadata: List<String> = listOf(),
    var timestamp: Long = -1L,
) {

    override fun toString(): String {
        return ReflectionToStringBuilder.reflectionToString(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (content != null) {
            if (other.content == null) return false
            if (!content.contentEquals(other.content)) return false
        } else if (other.content != null) return false
        if (contentType != other.contentType) return false
        if (topics != other.topics) return false
        if (senders != other.senders) return false
        if (metadata != other.metadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content?.contentHashCode() ?: 0
        result = 31 * result + (contentType?.hashCode() ?: 0)
        result = 31 * result + topics.hashCode()
        result = 31 * result + senders.hashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }
}
