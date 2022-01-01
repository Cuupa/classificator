package com.cuupa.classificator.engine.services.text

import com.cuupa.classificator.engine.RegexConstants
import com.cuupa.classificator.engine.StringConstants
import java.util.*

open class Text internal constructor(var text: String) {

    private val stringArray: Array<String>

    //normalize maps
    private val normalizeMap = mapOf(
        "\t" to " ",
        "\n\r" to " ",
        "\r\n" to " ",
        "\r" to " ",
        "\n" to " ",
        "," to " ",
        ": " to " ",
        "€" to " €",
        "/" to " ",
        "_" to " ",
        "Ãœ" to "ae",
        "ä" to "ae",
        "ã¼" to "ue",
        "ü" to "ue",
    )

    init {
        text = normalizeText(text)
        stringArray = text.split(StringConstants.blank).toTypedArray()
    }

    private fun normalizeText(text: String): String {
        var normalizedText = text.lowercase(Locale.getDefault())

        normalizeMap.entries.forEach {
            normalizedText = normalizedText.replace(it.key, it.value)
        }

        return normalizedText.replace(RegexConstants.twoBlanksRegex, " ").trim()
    }

    fun length() = stringArray.size

    operator fun get(index: Int): String {
        return stringArray[index]
    }

    fun isEmpty() = stringArray.isEmpty()


    override fun hashCode(): Int {
        return 31 * text.hashCode() + stringArray.contentHashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Text) return false

        if (text != other.text) return false
        if (!stringArray.contentEquals(other.stringArray)) return false

        return true
    }
}