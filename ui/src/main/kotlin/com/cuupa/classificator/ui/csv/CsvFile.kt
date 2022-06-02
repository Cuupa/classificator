package com.cuupa.classificator.ui.csv

import java.nio.charset.StandardCharsets

class CsvFile(content: ByteArray) {

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
                .filter { it != headlines }
                .map { CsvLine(headlines, it) }
        }
    }
}
