package com.cuupa.classificator.knowledgebase.services.text

import com.cuupa.classificator.constants.RegexConstants
import com.cuupa.classificator.constants.StringConstants

open class Text internal constructor(var text: String) {

    private val stringArray: Array<String>

    init {
        text = normalizeText(text)
        stringArray = text.split(StringConstants.blank).toTypedArray()
    }

    private fun normalizeText(text: String): String {
        return text.toLowerCase()
            .replace(StringConstants.tabstop, StringConstants.blank)
            .replace("\n\r", StringConstants.blank)
            .replace("\r\n", StringConstants.blank)
            .replace(StringConstants.carriageReturn, StringConstants.blank)
            .replace(StringConstants.newLine, StringConstants.blank)
            //		text = text.replace("-", StringConstants.BLANK);
            .replace(",", StringConstants.blank)
            .replace(": ", StringConstants.blank)
            .replace("€", " €")
            .replace("Ãœ", "ae")
            .replace("ä", "ae")
            .replace("ã¼", "ue")
            .replace("ü", "ue")
            .replace("/", StringConstants.blank)
            .replace("_", StringConstants.blank)
            .replace(RegexConstants.twoBlanksRegex, StringConstants.blank)
            .trim()
    }

    fun length(): Int {
        return stringArray.size
    }

    operator fun get(index: Int): String {
        return stringArray[index]
    }

    val isEmpty: Boolean
        get() = stringArray.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (other !is Array<*>) {
            return false
        }
        if (stringArray == other) {
            return true
        }
        if (stringArray.size != other.size) {
            return false
        }
        for (i in stringArray.indices) {
            if (stringArray[i] != other[i]) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return 31 * text.hashCode() + stringArray.contentHashCode()
    }
}