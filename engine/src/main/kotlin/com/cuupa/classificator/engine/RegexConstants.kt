package com.cuupa.classificator.engine

import org.apache.logging.log4j.util.Strings

object RegexConstants {
    val squareBracketOpenPattern = "\\[".toPattern()
    val squareBracketClosePattern = "]".toPattern()
    val equalPattern = StringConstants.equal.toPattern()

    val twoBlanksRegex = " {2}".toRegex()
    val emptyStringRegex = Strings.EMPTY.toRegex()
    val dotPattern = "\\.".toPattern()
}
