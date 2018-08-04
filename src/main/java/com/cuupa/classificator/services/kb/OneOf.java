package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.services.kb.semantic.PlainText;

public class OneOf extends Token {

	@Override
	public boolean match(String text) {
		PlainText plainText = new PlainText(text);
		for (String value : tokenValue) {
			if(plainText.contains(value)) {
				return true;
			}
		}
		return false;
	}

}
