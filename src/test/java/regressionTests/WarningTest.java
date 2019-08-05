package regressionTests;

import com.cuupa.classificator.services.kb.SemanticResult;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class WarningTest extends RegressionTest {

    @Value("${test.paths.warning}")
    private String path;

    @Test
    public void pathShouldNotBeEmpty() {
        assertNotNull(path);
    }

    @Test
    public void shouldBeWarnings() throws Exception {
        List<List<SemanticResult>> semanticResults = callSemantik(path);

        for (List<SemanticResult> results : semanticResults) {
            boolean found = false;
            for (SemanticResult result : results) {
                if (result.getTopicName().equals("WARNING")) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }
}
