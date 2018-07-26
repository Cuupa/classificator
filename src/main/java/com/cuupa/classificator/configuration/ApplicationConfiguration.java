package com.cuupa.classificator.configuration;

import org.springframework.context.annotation.Bean;

import com.cuupa.classificator.controller.ClassificatorController;
import com.cuupa.classificator.services.Classificiator;

public class ApplicationConfiguration {

	@Bean
	public ClassificatorController classificatorController() {
		return new ClassificatorController(classificator());
	}
	
	@Bean
	public Classificiator classificator() {
		return new Classificiator();
	}

}
