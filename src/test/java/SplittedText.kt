import com.cuupa.classificator.knowledgebase.stripper.TextAndPosition

class SplittedText {
    private var topText: List<TextAndPosition>? = null
    private var middleText: List<TextAndPosition>? = null
    private var bottomText: List<TextAndPosition>? = null
    fun setTopText(topText: List<TextAndPosition>?) {
        this.topText = topText
    }

    fun setMiddleText(middleText: List<TextAndPosition>?) {
        this.middleText = middleText
    }

    fun setBottomText(bottomText: List<TextAndPosition>?) {
        this.bottomText = bottomText
    }
}