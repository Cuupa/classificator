package regressionTests;

import com.cuupa.classificator.services.kb.SemanticResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import regressionTests.config.TestConfig;

import java.io.IOException;
import java.util.List;

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class SenderTest extends RegressionTest {

    private static final String path = "C:/Users/Simon/SynologyDrive/Desktop/Testdaten/Classificator/WARNING/[Mahnung]_BRN3C2AF4402B24_20181108_175433_001319.pdf";

    @Test
    public void shouldFindSender() throws IOException {
        List<List<SemanticResult>> semanticResults = callSemantikWithStructure(path);

        for (List<SemanticResult> results : semanticResults) {
            boolean found = false;
            for (SemanticResult result : results) {
                if (result.getTopicName().equals("RECEIPT_SLIP")) {
                    found = true;
                }
                System.out.println("Topic:" + result.getTopicName());
                System.out.println("Metadata:");
                result.getMetaData().stream().forEach(e -> System.out.println(e.getName() + "\n" + e.getValue()));
            }
        }
    }
}
