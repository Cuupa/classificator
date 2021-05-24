package regressionTests

import com.cuupa.classificator.engine.extensions.Extension.getText
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
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
        return PDDocument.load(file).use { it.getText() }.joinToString(separator = " ")
    }
}
