package com.cuupa.classificator.engine.services

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
        val month =  Calendar.getInstance().apply { time = date }.get(Calendar.MONTH) + 1
        return if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }
    }
}
