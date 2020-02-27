package com.cuupa.classificator.to

import java.io.Serializable

data class Document(val content: ByteArray, var filename: String?, var encrypted: Boolean) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (!content.contentEquals(other.content)) return false
        if (filename != other.filename) return false
        if (encrypted != other.encrypted) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.contentHashCode()
        result = 31 * result + (filename?.hashCode() ?: 0)
        result = 31 * result + encrypted.hashCode()
        return result
    }
}