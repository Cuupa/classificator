package com.cuupa.classificator.ui.csv

import java.nio.charset.StandardCharsets

class CsvFile(content: ByteArray) : Iterable<CsvLine> {

    val lines: List<CsvLine>

    val size: Int
        get() = lines.size

    init {
        val sanitizedContent = String(content, StandardCharsets.UTF_8).replace("\r", "")
        val contentLines = sanitizedContent.split("\n").filter { it.isNotEmpty() }

        lines = if (contentLines.isEmpty()) {
            listOf()
        } else {
            val headlines = contentLines[0]
            contentLines
                .filter { isContent(it) }
                .map { CsvLine(headlines, it) }
        }
    }

    private fun isContent(line: String): Boolean {
        return !line.contains(headlines.joinToString(";"))
    }

    companion object {
        val headlines = listOf("content", "tag")
    }

    override fun iterator() = lines.iterator()
}
