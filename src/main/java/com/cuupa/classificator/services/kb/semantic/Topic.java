package com.cuupa.classificator.services.kb.semantic;

import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import com.cuupa.classificator.services.kb.semantic.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Topic {

	public static final String OTHER = "OTHER";

	private final List<Token> tokenList = new ArrayList<>();

	private String topicName;

	@NotNull
	private List<MetaDataToken> metaDataToken = new ArrayList<>();

	public void addToken(Token token) {
		tokenList.add(token);
	}

	public String getName() {
		return topicName;
	}

	public void setName(String topicName) {
		this.topicName = topicName;
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

	@NotNull
	public List<Metadata> getMetaData(final String text) {
		List<Metadata> metadata = new ArrayList<>();
		for (MetaDataToken data : metaDataToken) {
			List<Metadata> extract = data.extract(text);
			metadata.addAll(extract);
		}

		return metadata;
	}

	@NotNull
	public List<MetaDataToken> getMetaDataList() {
		return metaDataToken;
	}

	public void setMetaDataList(@NotNull List<MetaDataToken> metaDataTokenList) {
		this.metaDataToken = new ArrayList<>(metaDataTokenList);
	}

	public void addMetaDataList(@NotNull List<MetaDataToken> metaDataTokenList) {
		metaDataToken.addAll(metaDataTokenList);
	}
}
