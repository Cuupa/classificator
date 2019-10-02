package com.cuupa.classificator.configuration.spring;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.controller.ClassificatorController;
import com.cuupa.classificator.controller.GuiController;
import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.KnowledgeBaseExecutorService;
import com.cuupa.classificator.services.kb.KnowledgeBaseInitiator;
import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.stripper.PdfAnalyser;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @NotNull
    @Bean
    public ClassificatorController classificatorController() {
        return new ClassificatorController(classificator());
    }

    @NotNull
    @Bean
    public GuiController guiController() {
        return new GuiController(classificator(), knowledgeManager());
    }

    @NotNull
    @Bean
    public Classificator classificator() {
        return new Classificator(knowledgeManager(), analyser());
    }

    @NotNull
    @Bean
    public KnowledgeManager knowledgeManager() {
        return new KnowledgeManager(knowledgeBaseInitiator(), knowledgeBaseExecutorService());
    }

    @NotNull
    @Bean
    public KnowledgeBaseExecutorService knowledgeBaseExecutorService() {
        return new KnowledgeBaseExecutorService();
    }

    @NotNull
    @Bean
    public KnowledgeBaseInitiator knowledgeBaseInitiator() {
        return new KnowledgeBaseInitiator(applicationProperties());
    }

    @NotNull
    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @NotNull
    @Bean
    public PdfAnalyser analyser() {
        return new PdfAnalyser();
    }
}
