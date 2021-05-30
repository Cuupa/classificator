package com.cuupa.classificator.engine.services.dataExtraction

import com.cuupa.classificator.engine.RegexConstants
import com.cuupa.classificator.engine.StringConstants
import com.cuupa.classificator.engine.services.Months

class DateExtract(regex: String) : Extract(Regex(regex.trim(), RegexOption.IGNORE_CASE)) {

    // TODO: normalize months in non numeric values
    override fun normalize(value: String): String {
        return if (value.contains(StringConstants.dot)) {
            val dateFields = value.split(RegexConstants.dotPattern)
            return when {
                dateFields.size == 3 -> normalizeDate(dateFields)
                dateFields.size == 2 && value.contains(StringConstants.blank) -> parseAndNormalizeDate(dateFields)
                else -> value
            }
        } else {
            value
        }
    }

    private fun parseAndNormalizeDate(dateFields: List<String>): String {
        val monthAndYear = dateFields[1].split(" ")
        return fill(dateFields[0], Months.get(monthAndYear[0]), monthAndYear[1])
    }

    private fun fill(day: String, month: String, year: String): String {
        val day1 = if(day.length == 1) "0$day" else day
        val month1 = if(month.length == 1) "0$month" else month
        val year1 = if(year.length == 2) "20$year" else year
        return "$day1.$month1.$year1"
    }

    private fun normalizeDate(dateFields: List<String>) = fill(dateFields[0], dateFields[1], dateFields[2])

    companion object {
        const val name = "[DATE]"
    }
}


