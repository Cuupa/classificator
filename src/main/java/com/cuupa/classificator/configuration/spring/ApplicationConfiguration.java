package com.cuupa.classificator.configuration.spring;

import com.cuupa.classificator.controller.ClassificatorController;
import com.cuupa.classificator.controller.GuiController;
import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.stripper.PdfAnalyser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public ClassificatorController classificatorController() {
		return new ClassificatorController(classificator());
	}

	@Bean
	public GuiController guiController() {
		return new GuiController(classificator());
	}

	@Bean
	public Classificator classificator() {
		return new Classificator(knowledgeManager(), analyser());
	}

	@Bean
	public KnowledgeManager knowledgeManager() {
		return new KnowledgeManager();
	}

    @Bean
    public PdfAnalyser analyser() {
        return new PdfAnalyser();
    }
}
