package com.cuupa.classificator.knowledgebase.services.knowledgebase.dataExtraction

import com.cuupa.classificator.knowledgebase.services.Months
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MonthsTest{

    @Test
    fun testJanuar(){
        var get = Months.get("January")
        assertEquals("01", get)
        get = Months.get("januar")
        assertEquals("01", get)
        get = Months.get("jan")
        assertEquals("01", get)
    }

    @Test
    fun testFebruar(){
        var get = Months.get("Februar")
        assertEquals("02", get)
        get = Months.get("februar")
        assertEquals("02", get)
        get = Months.get("feb")
        assertEquals("02", get)
    }
}