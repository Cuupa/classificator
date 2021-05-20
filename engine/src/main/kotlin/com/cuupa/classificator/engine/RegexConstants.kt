package com.cuupa.classificator.engine

object RegexConstants {
    val squareBracketOpenPattern = "\\[".toPattern()
    val squareBracketClosePattern = "]".toPattern()
    val equalPattern = StringConstants.equal.toPattern()

    val twoBlanksRegex = " {2}".toRegex()
    val emptyStringRegex = "".toRegex()
    val dotPattern = "\\.".toPattern()
}
