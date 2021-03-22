package com.cuupa.classificator.knowledgebase.stripper

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

internal class PdfAnalyserTest {

    private val unitToTest = PdfAnalyser()

    private val pdf = File("C:\\Users\\Simon\\SynologyDrive\\Schreibtisch\\in.pdf")
    private val output = File("C:\\Users\\Simon\\SynologyDrive\\Schreibtisch\\out.pdf")
/*
    @Test
    fun getResults() {
        Files.copy(pdf.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING)
        PDDocument.load(Files.readAllBytes(pdf.toPath())).use {
            val results = unitToTest.getResults(it)

            results.forEachIndexed { index, list ->
                PDPageContentStream(
                    it,
                    it.getPage(index),
                    PDPageContentStream.AppendMode.APPEND,
                    true
                ).use { contentStream ->
                    contentStream.setFont(PDType1Font.COURIER, 10f);
                    contentStream.setNonStrokingColor(Color.RED)
                    val outlines = computeOutline(list)
                    outlines.forEach { outline -> contentStream.addRect(
                        outline.x,
                        outline.y,
                        outline.width,
                        outline.height
                    ) }
                    contentStream.fill()
                }
            }
            it.save(output)
        }
    }*/
/*
    private fun computeOutline(list: List<TextAndPosition>): List<Outline> {
       // val value = mutableListOf<Outline>()

       // list.filter { it.isEmpty() }

        //var outline = Outline()
        var x = 0.0f
        var y = 0.0f
        for (index in list.indices) {
            if(list[index].value == "Herrn") {
                println()
            }
            if (list[index].width > tenPercent(getOrDefault(list, index).x))
             {
                value.add(outline)
                x = 0.0f
                outline = Outline()
            }
            if (x == 0.0f) {
                x = list[index].x
                outline.x = x
            }
            outline.width += list[index].width
            outline.height += list[index].height
            outline.y += list[index].y
        }

        return value
    }*/

    private fun getOrDefault(
        list: List<TextAndPosition>,
        index: Int
    ): TextAndPosition {
        if (index + 1 == list.size) {
            return list[index]
        }
        return list[index + 1]
    }

    private fun tenPercent(float: Float) = float / 100 * 10
}