package com.cuupa.classificator.services.kb.semantic.dataExtraction

import java.text.SimpleDateFormat
import java.util.*

object Months {

    fun get(value: String): String {
        val date = SimpleDateFormat("MMMM").parse(value)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val month = calendar.get(Calendar.MONTH) + 1
        return if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }
    }
}
