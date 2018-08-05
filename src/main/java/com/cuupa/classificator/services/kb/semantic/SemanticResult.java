package com.cuupa.classificator.services.kb.semantic;

import java.util.List;

public class SemanticResult {

	private String topicName;

	private List<Metadata> metaData;

	public SemanticResult(String topicName) {
		this.topicName = topicName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setMetaData(List<Metadata> metaData) {
		this.metaData = metaData;
	}

	public List<Metadata> getMetaData() {
		return metaData;
	}
}
