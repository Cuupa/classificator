package regressionTests.config;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.stripper.PdfAnalyser;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
@Configuration
public class TestConfig {
    @NotNull
    @Bean
    public KnowledgeManager knowledgeManager() {
        return new KnowledgeManager(applicationProperties());
    }

    @NotNull
    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @NotNull
    @Bean
    public Classificator classificator() {
        return new Classificator(knowledgeManager(), analyser());
    }

    @NotNull
    @Bean
    public PdfAnalyser analyser() {
        return new PdfAnalyser();
    }
}
