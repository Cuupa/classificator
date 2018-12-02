package com.cuupa.classificator.services;

import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;

import java.util.ArrayList;
import java.util.List;

public class Classificiator {
	
	private KnowledgeManager manager;

	public Classificiator(KnowledgeManager manager) {
		this.manager = manager;
	}

	public List<SemanticResult> classifiy(final String text) {
		if(text == null || text.length() == 0) {
			return new ArrayList<>();
		}
		
		List<SemanticResult> results = manager.getResults(text);
		if(results.size() == 0) {
			results.add(new SemanticResult("OTHER"));
		}
		
		return results;
	}

	public List<SemanticResult> classifiy(byte[] content) {
		return null;
	}
}
