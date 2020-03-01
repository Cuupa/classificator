package com.cuupa.classificator.services.kb.semantic.text

open class Text internal constructor(var text: String) {

    private val stringArray: Array<String>?

    init {
        text = normalizeText(text)
        stringArray = text.split(BLANK)
                .toTypedArray()
    }

    private fun normalizeText(text: String): String {
        var value = text
        value = value.toLowerCase()
        value = value.replace("\t", BLANK)
        value = value.replace("\n\r", BLANK)
        value = value.replace("\r\n", BLANK)
        value = value.replace("\r", BLANK)
        value = value.replace("\n", BLANK)
        value = value.replace("\t", BLANK)
        //		text = text.replace("-", BLANK);
        value = value.replace(",", BLANK)
        value = value.replace(": ", BLANK)
        value = value.replace("€", " €")
        value = value.replace("Ãœ", "ae")
        value = value.replace("ä", "ae")
        value = value.replace("ã¼", "ue")
        value = value.replace("ü", "ue")
        value = value.replace("/", BLANK)
        value = value.replace("_", BLANK)
        value = value.replace(" {2}".toRegex(), " ")
        return value.trim { it <= ' ' }
    }

    fun length(): Int {
        return stringArray?.size ?: 0
    }

    operator fun get(currentPositionSearchString: Int): String {
        return stringArray!![currentPositionSearchString]
    }

    val isEmpty: Boolean
        get() = stringArray == null || stringArray.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (other !is Array<*>) {
            return false
        }
        if (stringArray == other) {
            return true
        }
        if (stringArray!!.size != other.size) {
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
        result = 31 * result + (stringArray?.contentHashCode() ?: 0)
        return result
    }

    companion object {
        private const val BLANK = " "
    }
}