package com.cuupa.classificator.engine.services.dataExtraction

import com.cuupa.classificator.engine.RegexConstants
import com.cuupa.classificator.engine.services.Months

class DateExtract(regex: String) : Extract(Regex(regex.trim(), RegexOption.IGNORE_CASE)) {

    // TODO: normalize months in non numeric values
    override fun normalize(value: String): String {
        if (value.contains(".")) {
            val dateFields = value.split(RegexConstants.dotPattern)
            return when {
                dateFields.size == 3 -> normalizeDate(dateFields)
                dateFields.size == 2 && value.contains(" ") -> parseAndNormalizeDate(dateFields)
                else -> value
            }
        }
        return value
    }

    private fun parseAndNormalizeDate(dateFields: List<String>): String {
        val monthAndYear = dateFields[1].split(" ")
        return fill(dateFields[0], Months.get(monthAndYear[0]), monthAndYear[1])
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

    private fun normalizeDate(dateFields: List<String>) = fill(dateFields[0], dateFields[1], dateFields[2])

    companion object {
        const val name = "[DATE]"
    }
}


