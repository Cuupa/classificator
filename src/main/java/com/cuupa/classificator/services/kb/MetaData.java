package com.cuupa.classificator.services.kb;

import java.util.ArrayList;
import java.util.List;

public class MetaData {

	private String name;

	private List<Token> token = new ArrayList<>();
	
	public void setName(String name) {
		this.name = name;
	}

	public void addToken(Token token) {
		this.token.add(token);
	}

	public void extract(String text) {
		for (Token token2 : token) {
			List<String> tokenValue = token2.tokenValue;
			
			for (int i = 0; i < tokenValue.size(); i++) {
				String string = tokenValue.get(i).split("\\[")[0];
			}
			
			token2.match(text);
		}
	}

	private Token compileToken(Token token2) {
		
		return null;
	}
}
