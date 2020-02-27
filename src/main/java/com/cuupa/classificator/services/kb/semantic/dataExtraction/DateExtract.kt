package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.util.regex.Pattern

class DateExtract(regex: String) : Extract(Pattern.compile(regex)) {

    override fun normalize(value: String): String {
        val split = value.split("\\.".toPattern())
        var day = split[0]
        var month = split[1]
        var year = split[2]
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
}