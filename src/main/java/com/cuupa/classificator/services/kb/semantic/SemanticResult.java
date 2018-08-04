package com.cuupa.classificator.services.kb.semantic;

import java.util.List;

public class SemanticResult {

	private String topicName;
	
	private List<Metadata> metadata;
	
	public SemanticResult(String topicName) {
		this.topicName = topicName;
	}
}
