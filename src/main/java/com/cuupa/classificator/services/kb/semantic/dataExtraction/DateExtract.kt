package com.cuupa.classificator.services.kb.semantic.dataExtraction

import com.cuupa.classificator.constants.RegexConstants
import java.util.regex.Pattern

class DateExtract(regex: String) : Extract(Pattern.compile(regex)) {

    override fun normalize(value: String): String {
        val dateFields = value.split(RegexConstants.dotPattern)
        var day = dateFields[0]
        var month = dateFields[1]
        var year = dateFields[2]
        if (day.length == 1) {
            day = "0$day"
        }
        if (month.length == 1) {
            month = "0$month"
        }
        if (year.length == 2) {
            year = "20$year"
        }
        return "$day.$month.$year"
    }

    companion object {
        const val name = "[DATE]"
    }
}