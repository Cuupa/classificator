import com.cuupa.classificator.knowledgebase.services.text.TextSearch
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TextSearchTest {

    @Test
    fun shouldFindText() {
        val text = TextSearch("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz")
        val contains = text.contains("ipsum dolor")
        assertTrue(contains)
    }

    @Test
    fun shouldNotFindText() {
        val text = TextSearch("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz")
        val contains = text.contains("lorem ipsum dolor")
        assertFalse(contains)
    }

    @Test
    fun shouldHaveThreeOccurences() {
        val textSearch = TextSearch(
                "abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz")
        val numberOfOccurences = textSearch.countOccurence("Sparkasse Krefeld")
        assertEquals(3, numberOfOccurences.toLong())
    }

    @Test
    fun shouldHaveOneOccurences() {
        val textSearch = TextSearch(
                "Sehr geehrte Damen und Herren. Hiermit kündige ich meinen Vertrag bei Ihnen. Mit freundlichen Grüßen, Max Mustermann")
        var numberOfOccurences = textSearch.countOccurence("Vertrag")
        assertEquals(1, numberOfOccurences.toLong())
        numberOfOccurences = textSearch.countOccurence("Vertrag")
        assertEquals(1, numberOfOccurences.toLong())
        numberOfOccurences = textSearch.countOccurence("Max Mustermann")
        assertEquals(1, numberOfOccurences.toLong())
    }
}