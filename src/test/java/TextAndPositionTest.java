import com.cuupa.classificator.services.stripper.GetLocationAndSizeStripper;
import com.cuupa.classificator.services.stripper.TextAndPosition;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextAndPositionTest {

    private File testFile = new File("C:/Users/Simon/Desktop/test.pdf");

    @Test
    public void dingens() {
        try (PDDocument document = PDDocument.load(testFile)) {
            GetLocationAndSizeStripper stripper = new GetLocationAndSizeStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(1);
            List<TextAndPosition> textAndPositions = stripper.getTextAndPositions(document);

            PDPage page = document.getPage(1);
            PDRectangle cropBox = page.getCropBox();

            List<List<TextAndPosition>> lists = splitVerticaly(cropBox, textAndPositions);
            splitHorizontly(cropBox, lists.get(0));
            splitHorizontly(cropBox, lists.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void splitHorizontly(PDRectangle cropBox, List<TextAndPosition> textAndPositions) {

        int sizePerPart = Math.round(cropBox.getHeight() / 3);
        int top = sizePerPart;
        int middle = sizePerPart + top;
        int bottom = middle + sizePerPart;

        List<TextAndPosition> topText = getTextOfHeight(textAndPositions, 0, top);
        List<TextAndPosition> middleText = getTextOfHeight(textAndPositions, top, middle);
        List<TextAndPosition> bottomText = getTextOfHeight(textAndPositions, middle, bottom);

        for (TextAndPosition text : topText) {
            System.out.println(text.getValue());
        }

        for (TextAndPosition text : middleText) {
            System.out.println(text.getValue());
        }

        for (TextAndPosition text : bottomText) {
            System.out.println(text.getValue());
        }
    }

    private List<List<TextAndPosition>> splitVerticaly(PDRectangle cropBox, List<TextAndPosition> textAndPositions) {
        int leftSide = Math.round(cropBox.getWidth() / 2);
        int rightSide = Math.round(cropBox.getWidth() - leftSide);

        List<TextAndPosition> textLeftSide = getTextOfWidth(textAndPositions, 0, leftSide);
        List<TextAndPosition> textRightSide = getTextOfWidth(textAndPositions, rightSide, Math.round(cropBox.getWidth()));

        List<List<TextAndPosition>> value = new ArrayList<>();
        value.add(textLeftSide);
        value.add(textRightSide);
        return value;
    }

    private List<TextAndPosition> getTextOfWidth(List<TextAndPosition> textAndPositions, int start, int end) {
        return textAndPositions.stream()
                .filter(e -> e.getX() >= start && e.getX() <= end)
                .collect(Collectors.toList());
    }

    private List<TextAndPosition> getTextOfHeight(List<TextAndPosition> textAndPositions, int start, int end) {
        return textAndPositions.stream()
                .filter(e -> e.getY() >= start && e.getY() <= end)
                .collect(Collectors.toList());
    }
}
