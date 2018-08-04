package com.cuupa.classificator.configuration.spring;

import org.springframework.context.annotation.Bean;

import com.cuupa.classificator.controller.ClassificatorController;
import com.cuupa.classificator.services.Classificiator;
import com.cuupa.classificator.services.kb.KnowledgeManager;

public class ApplicationConfiguration {

	@Bean
	public ClassificatorController classificatorController() {
		return new ClassificatorController(classificator());
	}
	
	@Bean
	public Classificiator classificator() {
		return new Classificiator(knowledgeManager());
	}

	@Bean
	public KnowledgeManager knowledgeManager() {
		return new KnowledgeManager();
	}
}
