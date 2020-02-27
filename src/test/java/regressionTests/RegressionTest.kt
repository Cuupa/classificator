package regressionTests

import com.cuupa.classificator.services.Classificator
import com.cuupa.classificator.services.kb.SemanticResult
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors

abstract class RegressionTest {
    @Autowired
    private val classificator: Classificator? = null

    @Throws(IOException::class)
    protected fun callSemantik(path: String): List<List<SemanticResult>> {
        val contents = Files.list(Paths.get(path))
                .map { path: Path -> read(path) }
                .collect(Collectors.toList())
        val semanticResults: MutableList<List<SemanticResult>> = ArrayList()
        for (string in contents) {
            val strings = string.stream()
                    .map { o: String? -> Objects.toString(o) }
                    .collect(Collectors.joining())
            semanticResults.add(classificator!!.classify(strings))
        }
        return semanticResults
    }

    @Throws(IOException::class)
    protected fun callSemantikWithStructure(
            path: String): List<List<SemanticResult>> {
        val bytes = Files.readAllBytes(Paths.get(path))
        val semanticResults: MutableList<List<SemanticResult>> = ArrayList()
        semanticResults.add(classificator!!.classify(bytes))
        return semanticResults
    }

    protected fun read(path: Path): List<String?> {
        try {
            PDDocument.load(Files.readAllBytes(path)).use { document ->
                val pages: MutableList<String?> = ArrayList(document.numberOfPages)
                for (page in 1..document.numberOfPages) {
                    val stripper = PDFTextStripper()
                    stripper.startPage = page
                    stripper.endPage = page
                    pages.add(stripper.getText(document))
                }
                return pages
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return emptyList<String>()
    }
}