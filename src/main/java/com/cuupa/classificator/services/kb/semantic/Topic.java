package com.cuupa.classificator.services.kb.semantic;

import java.util.ArrayList;
import java.util.List;

import com.cuupa.classificator.services.kb.MetaData;
import com.cuupa.classificator.services.kb.Token;

public class Topic {

	private String topicName;

	private List<Token> tokenList = new ArrayList<>();

	private List<MetaData> metaData = new ArrayList<>();

	public void setName(String topicName) {
		this.topicName = topicName;
	}

	public void addToken(Token token) {
		tokenList.add(token);
	}

	public String getName() {
		return topicName;
	}

	public boolean match(String text) {
		for (Token token : tokenList) {
			if (!token.match(text)) {
				return false;
			}
		}
		return true;
	}

	public void addMetaData(MetaData metadata) {
		this.metaData.add(metadata);
	}

	public void getMetaData(String text) {
		for (MetaData data : metaData) {
			data.extract(text);
		}
	}
}
