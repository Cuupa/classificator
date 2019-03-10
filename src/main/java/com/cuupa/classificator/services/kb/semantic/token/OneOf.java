package com.cuupa.classificator.services.kb.semantic.token;

import java.util.ArrayList;
import com.cuupa.classificator.services.kb.semantic.PlainText;

public class OneOf extends Token {
	
	private int distance;

	@Override
	public boolean match(String text) {
		PlainText plainText = new PlainText(text);
		for (String value : tokenValue) {
			if(plainText.contains(value)) {
				distance = plainText.getDistance();
				return true;
			}
		}
		return false;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	protected Token clone() {
		Token token = new OneOf();
		token.setTokenValue(new ArrayList<>(tokenValue));
		return token;
	}
	
	@Override
	public String toString() {
		return "OneOf " + super.toString();
	}
}
