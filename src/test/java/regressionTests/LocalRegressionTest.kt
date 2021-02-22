package regressionTests

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

open class LocalRegressionTest {

    fun getFilesOfPath(stringPath: String): List<File> {
        val path = Paths.get(stringPath)
        return if (path.toFile().exists()) {
            Files.list(path).filter { it.toFile().name.endsWith(".pdf") }.map { it.toFile() }
                .collect(Collectors.toList()) ?: listOf()
        } else {
            listOf()
        }
    }

    fun getFileContent(file: File): String {
        return PDDocument.load(file).use { getTextPerPage(it) }.joinToString(" ", "", "")
    }

    private fun getTextPerPage(document: PDDocument): List<String> {
        val pages: MutableList<String> = ArrayList(document.numberOfPages)
        for (page in 0..document.numberOfPages) {
            val stripper = PDFTextStripper()
            stripper.startPage = page
            stripper.endPage = page
            pages.add(stripper.getText(document))
        }
        return pages
    }
}
