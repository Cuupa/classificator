import com.cuupa.classificator.engine.stripper.LocationAndSizeStripper
import com.cuupa.classificator.engine.stripper.TextAndPosition
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import java.io.File
import java.io.IOException
import java.util.stream.Collectors
import kotlin.math.roundToInt

class TextAndPositionTest {
    private val testFile = File("")

    fun dingens() {
        try {
            PDDocument.load(testFile).use { document ->
                val stripper = LocationAndSizeStripper()
                stripper.startPage = 1
                stripper.endPage = 1
                val textAndPositions = stripper.getTextAndPositions(document)
                val page = document.getPage(1)
                val cropBox = page.cropBox
                val lists = splitVerticaly(cropBox, textAndPositions)
                splitHorizontly(cropBox, lists[0])
                splitHorizontly(cropBox, lists[1])
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun splitHorizontly(cropBox: PDRectangle,
                                textAndPositions: List<TextAndPosition>) {
        val sizePerPart = (cropBox.height / 3).roundToInt()
        val middle = sizePerPart + sizePerPart
        val bottom = middle + sizePerPart
        val topText = getTextOfHeight(textAndPositions, 0, sizePerPart)
        val middleText = getTextOfHeight(textAndPositions, sizePerPart, middle)
        val bottomText = getTextOfHeight(textAndPositions, middle, bottom)
        for (text in topText) {
            println(text.value)
        }
        for (text in middleText) {
            println(text.value)
        }
        for (text in bottomText) {
            println(text.value)
        }
    }

    private fun splitVerticaly(cropBox: PDRectangle,
                               textAndPositions: List<TextAndPosition>): List<List<TextAndPosition>> {
        val leftSide = (cropBox.width / 2).roundToInt()
        val rightSide = (cropBox.width - leftSide).roundToInt()
        val textLeftSide = getTextOfWidth(textAndPositions, 0, leftSide)
        val textRightSide = getTextOfWidth(textAndPositions, rightSide, cropBox.width.roundToInt())
        val value: MutableList<List<TextAndPosition>> = ArrayList()
        value.add(textLeftSide)
        value.add(textRightSide)
        return value
    }

    private fun getTextOfWidth(textAndPositions: List<TextAndPosition>, start: Int,
                               end: Int): List<TextAndPosition> {
        return textAndPositions.stream()
                .filter { e: TextAndPosition -> e.x >= start && e.x <= end }
                .collect(Collectors.toList())
    }

    private fun getTextOfHeight(textAndPositions: List<TextAndPosition>, start: Int,
                                end: Int): List<TextAndPosition> {
        return textAndPositions.stream()
                .filter { e: TextAndPosition -> e.y >= start && e.y <= end }
                .collect(Collectors.toList())
    }
}