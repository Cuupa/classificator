package com.cuupa.classificator.services.kb.semantic.token;

import java.util.ArrayList;

import com.cuupa.classificator.services.kb.semantic.PlainText;

public class All extends Token {
	
	private int distance;

	@Override
	public boolean match(String text) {
		PlainText plainText = new PlainText(text);
		for (String value : tokenValue) {
			if(!plainText.contains(value)) {
				distance = plainText.getDistance();
				return false;
			}
		}
		return true;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	protected Token clone() {
		Token token = new All();
		token.setTokenValue(new ArrayList<>(tokenValue));
		return token;
	}
	
	@Override
	public String toString() {
		return "ALL " + super.toString();
	}
}
