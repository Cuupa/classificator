package com.cuupa.classificator.services.kb.semantic.token;

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
}
