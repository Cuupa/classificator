package com.cuupa.classificator.ui.csv

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

open class CsvFileTest {

    private val content = "content;contentType;tag;\nHello World;text/plain;test;".toByteArray()

    @Test
    open fun shouldParse(){
        val file = CsvFile(content)
        assertNotNull(file)
    }

    @Test
    open fun shouldParseHaveContent(){
        val file = CsvFile(content)
        assertEquals(1, file.size)
        assertNotNull(file.lines[0])
        assertEquals("Hello World", file.lines[0].content)
    }

    @Test
    open fun shouldParseHaveTag(){
        val file = CsvFile(content)
        assertEquals(1, file.size)
        assertNotNull(file.lines[0])
        assertEquals("test", file.lines[0].tag)
    }

    @Test
    open fun shouldParseHaveContentType(){
        val file = CsvFile(content)
        assertEquals(1, file.size)
        assertNotNull(file.lines[0])
        assertEquals("text/plain", file.lines[0].tag)
    }
}