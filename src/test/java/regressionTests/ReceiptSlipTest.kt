package regressionTests;

import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.SemanticResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import regressionTests.config.TestConfig;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ReceiptSlipTest extends RegressionTest {

    @Autowired
    private Classificator classificator;

    @Value("${test.paths.receipt_slip}")
    private String path;

    @Test
    public void pathShouldNotBeEmpty() {
        assertNotNull(path);
    }

    @Test
    public void shouldBeReceiptSlip() throws Exception {
        List<List<SemanticResult>> semanticResults = callSemantik(path);

        for (List<SemanticResult> results : semanticResults) {
            boolean found = false;
            for (SemanticResult result : results) {
                if (result.getTopicName().equals("RECEIPT_SLIP")) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }
}
