package com.cuupa.classificator.services.kb.semantic.text

open class Text internal constructor(var text: String) {

    private val stringArray: Array<String>

    init {
        text = normalizeText(text)
        stringArray = text.split(BLANK).toTypedArray()
    }

    private fun normalizeText(text: String): String {
        return text.toLowerCase()
            .replace("\t", BLANK)
            .replace("\n\r", BLANK)
            .replace("\r\n", BLANK)
            .replace("\r", BLANK)
            .replace("\n", BLANK)
            .replace("\t", BLANK)
            //		text = text.replace("-", BLANK);
            .replace(",", BLANK)
            .replace(": ", BLANK)
            .replace("€", " €")
            .replace("Ãœ", "ae")
            .replace("ä", "ae")
            .replace("ã¼", "ue")
            .replace("ü", "ue")
            .replace("/", BLANK)
            .replace("_", BLANK)
            .replace(twoBlanksRegex, BLANK)
            .trim()
    }

    fun length(): Int {
        return stringArray.size
    }

    operator fun get(currentPositionSearchString: Int): String {
        return stringArray[currentPositionSearchString]
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
        var result = text.hashCode()
        result = 31 * result + (stringArray.contentHashCode() ?: 0)
        return result
    }

    companion object {
        private const val BLANK = " "
        private val twoBlanksRegex = " {2}".toRegex()
    }
}