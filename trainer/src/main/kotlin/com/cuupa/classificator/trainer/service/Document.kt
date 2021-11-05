package com.cuupa.classificator.trainer.service

import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import java.util.*

data class Document(
    val id: UUID? = null,
    val content: ByteArray? = null,
    val contentType: String? = null,
    val plainText: String? = null,
    val results: List<String> = listOf(),
    val senders: List<String> = listOf(),
    val metadata: List<String> = listOf(),
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
        if (results != other.results) return false
        if (senders != other.senders) return false
        if (metadata != other.metadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content?.contentHashCode() ?: 0
        result = 31 * result + (contentType?.hashCode() ?: 0)
        result = 31 * result + results.hashCode()
        result = 31 * result + senders.hashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }
}
