package com.cuupa.classificator.services;

import java.util.ArrayList;
import java.util.List;

import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;

public class Classificiator {
	
	private KnowledgeManager manager;

	public Classificiator(KnowledgeManager manager) {
		this.manager = manager;
	}

	public List<SemanticResult> classifiy(final String text) {
		final List<SemanticResult> results = new ArrayList<>();
		
		if(text == null || text.length() == 0) {
			return results;
		}
		
		results.addAll(manager.getResults(text));
		
		return results;
	}

}
