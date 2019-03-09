package com.cuupa.classificator.services.kb.semantic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import com.cuupa.classificator.services.kb.semantic.token.Token;

public class Topic {

	private String topicName;

	private final List<Token> tokenList = new ArrayList<>();

	private List<MetaDataToken> metaDataToken = new ArrayList<>();

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

	public void addMetaData(MetaDataToken metadata) {
		this.metaDataToken.add(metadata);
	}

	public List<Metadata> getMetaData(String text) {
		List<Metadata> metadata = new ArrayList<>();
		for (MetaDataToken data : metaDataToken) {
			List<Metadata> extract = data.extract(text);
			metadata.addAll(extract);
		}

		return metadata;
	}

	public void setMetaDataList(List<MetaDataToken> metaDataTokenList) {
		this.metaDataToken = new ArrayList<>(metaDataTokenList);
	}

	public List<MetaDataToken> getMetaDataList() {
		return metaDataToken;
	}

	public void addMetaDataList(List<MetaDataToken> metaDataTokenList) {
		metaDataToken.addAll(metaDataTokenList);
	}
}
