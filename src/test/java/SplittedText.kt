import java.util.List;

import com.cuupa.classificator.services.stripper.TextAndPosition;

public class SplittedText {

	private List<TextAndPosition> topText;
	private List<TextAndPosition> middleText;
	private List<TextAndPosition> bottomText;

	public void setTopText(List<TextAndPosition> topText) {
		this.topText = topText;
	}

	public void setMiddleText(List<TextAndPosition> middleText) {
		this.middleText = middleText;
	}

	public void setBottomText(List<TextAndPosition> bottomText) {
		this.bottomText = bottomText;
	}

}
