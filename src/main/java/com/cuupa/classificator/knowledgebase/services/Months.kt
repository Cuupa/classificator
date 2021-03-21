package com.cuupa.classificator.knowledgebase.services

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object Months {

    fun get(value: String): String {
        val date = try {
            SimpleDateFormat("MMMM").parse(value)
        } catch (e: Exception) {
            return value
        }
        val calendar = Calendar.getInstance().apply { time = date }
        val month = calendar.get(Calendar.MONTH) + 1
        return if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }
    }
}
