package com.cuupa.classificator.services.kb.semantic.dataExtraction

import com.cuupa.classificator.constants.RegexConstants
import java.util.regex.Pattern

class DateExtract(regex: String) : Extract(Pattern.compile(regex, Pattern.CASE_INSENSITIVE)) {

    // TODO: normalize months in non numeric values
    override fun normalize(value: String): String {
        if (value.contains(".")) {
            val dateFields = value.split(RegexConstants.dotPattern)
            return when (dateFields.size) {
                3 -> normalizeDate(dateFields)
                2 -> parseAndNormalizeDate(dateFields)
                else -> value
            }
        }
        return value
    }

    private fun parseAndNormalizeDate(dateFields: List<String>): String {
        var day = dateFields[0]
        var monthAndYear = dateFields[1].split(" ")
        var month = Months.get(monthAndYear[0])
        var year = monthAndYear[1]
        return fill(day, month, year)
    }

    private fun fill(day: String, month: String, year: String): String {
        var day1 = day
        var month1 = month
        var year1 = year
        if (day1.length == 1) {
            day1 = "0$day1"
        }
        if (month1.length == 1) {
            month1 = "0$month1"
        }
        if (year1.length == 2) {
            year1 = "20$year1"
        }
        return "$day1.$month1.$year1"
    }

    private fun normalizeDate(dateFields: List<String>): String {
        var day = dateFields[0]
        var month = dateFields[1]
        var year = dateFields[2]
        return fill(day, month, year)
    }

    companion object {
        const val name = "[DATE]"
    }
}


