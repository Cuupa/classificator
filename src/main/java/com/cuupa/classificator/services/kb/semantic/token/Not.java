package com.cuupa.classificator.services.kb.semantic.token;

import com.cuupa.classificator.services.kb.semantic.PlainText;

public class Not extends Token {

	@Override
	public boolean match(String text) {
		PlainText plaintext = new PlainText(text);
		for (String token : tokenValue) {
			if(plaintext.contains(token)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getDistance() {
		return 0;
	}
}
