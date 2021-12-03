package com.cuupa.classificator.engine.services

import org.apache.tika.Tika
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class TextExtractorTest {

    private val tika = Tika()

    private val unitToTest = TextExtractor(tika)

    @Test
    fun shouldBeEmpty() {
        val result = unitToTest.extractText(null, ByteArray(0))

        assertNotNull(result)
        assertEquals(MediaType.APPLICATION_OCTET_STREAM_VALUE, result.contentType)
        assertNull(result.content)
    }

    @Test
    fun shouldExtractPlainText() {
        val payload = "Hello World".encodeToByteArray()

        val result = unitToTest.extractText(MediaType.TEXT_PLAIN_VALUE, payload)

        assertNotNull(result)
        assertEquals(MediaType.TEXT_PLAIN_VALUE, result.contentType)
        assertEquals("Hello World", result.content)
    }

    @Test
    fun shouldExtractPdfText() {
        val payload = javaClass.classLoader.getResourceAsStream("hello_world.pdf").readAllBytes()

        val result = unitToTest.extractText(MediaType.APPLICATION_PDF_VALUE, payload)

        assertNotNull(result)
        assertEquals(MediaType.APPLICATION_PDF_VALUE, result.contentType)
        assertEquals("Hello World", result.content?.trim())
    }

    //@Test
    // TODO: tika-parsers are required for this test
    fun shouldExtractPdfWithoutHints() {
        val payload = javaClass.classLoader.getResourceAsStream("hello_world.pdf").readAllBytes()

        val result = unitToTest.extractText(null, payload)

        assertNotNull(result)
        assertEquals(MediaType.APPLICATION_PDF_VALUE, result.contentType)
        assertEquals("Hello World\n", result.content)
    }

    @Test
    fun shouldExtractPlainTextWithoutHints() {
        val payload = "Hello World".encodeToByteArray()

        val result = unitToTest.extractText(MediaType.TEXT_PLAIN_VALUE, payload)

        assertNotNull(result)
        assertEquals(MediaType.TEXT_PLAIN_VALUE, result.contentType)
        assertEquals("Hello World", result.content)
    }
}