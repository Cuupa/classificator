import com.cuupa.classificator.services.kb.semantic.text.TextSearch
import org.junit.Assert
import org.junit.Test

class TextSearchTest {
    @Test
    fun shouldFindText() {
        val text = TextSearch("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz")
        val contains = text.contains("ipsum dolor")
        Assert.assertTrue(contains)
    }

    @Test
    fun shouldNotFindText() {
        val text = TextSearch("abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor sit amet xyz")
        val contains = text.contains("lorem ipsum dolor")
        Assert.assertFalse(contains)
    }

    @Test
    fun shouldHaveThreeOccurences() {
        val textSearch = TextSearch(
                "abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz abc lorem sdsdfhsdhfsduhfsdhfshdfu ipsum dolor Sparkasse Krefeld sit amet xyz")
        val numberOfOccurences = textSearch.countOccurence("Sparkasse Krefeld")
        Assert.assertEquals(3, numberOfOccurences.toLong())
    }

    @Test
    fun shouldHaveOneOccurences() {
        val textSearch = TextSearch(
                "Sehr geehrte Damen und Herren. Hiermit kündige ich meinen Vertrag bei Ihnen. Mit freundlichen Grüßen, Max Mustermann")
        var numberOfOccurences = textSearch.countOccurence("Vertrag")
        Assert.assertEquals(1, numberOfOccurences.toLong())
        numberOfOccurences = textSearch.countOccurence("Vertrag")
        Assert.assertEquals(1, numberOfOccurences.toLong())
        numberOfOccurences = textSearch.countOccurence("Max Mustermann")
        Assert.assertEquals(1, numberOfOccurences.toLong())
    }
}