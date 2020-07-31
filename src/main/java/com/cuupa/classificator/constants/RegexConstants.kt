package com.cuupa.classificator.constants

import jdk.internal.joptsimple.internal.Strings

object RegexConstants {
    val squareBracketOpenPattern = "\\[".toPattern()
    val squareBracketClosePattern = "]".toPattern()
    val equalPattern = StringConstants.equal.toPattern()

    val twoBlanksRegex = " {2}".toRegex()
    val emptyStringRegex = Strings.EMPTY.toRegex()
    val dotPattern = "\\.".toPattern()
}
