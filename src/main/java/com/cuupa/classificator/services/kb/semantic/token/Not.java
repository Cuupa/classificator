package com.cuupa.classificator.services.kb.semantic.token;

import com.cuupa.classificator.services.kb.semantic.text.TextSearch;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Not extends Token {

	@Override
	public boolean match(String text) {
        TextSearch plaintext = new TextSearch(text);
		for (String token : tokenValue) {
			if (plaintext.contains(token)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getDistance() {
		return 0;
	}

	@NotNull
	@Override
	protected Token clone() {
		Token token = new Not();
		token.setTokenValue(new ArrayList<>(tokenValue));
		return token;
	}

	@NotNull
	@Override
	public String toString() {
		return "NOT " + super.toString();
	}
}
