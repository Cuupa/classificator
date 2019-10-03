package com.cuupa.classificator.services.kb.semantic.token;

import com.cuupa.classificator.services.kb.semantic.text.TextSearch;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OneOf extends Token {

	private int distance;

	@Override
	public boolean match(String text) {
        TextSearch textSearch = new TextSearch(text);
		for (String value : tokenValue) {
            if (textSearch.contains(value)) {
                distance = textSearch.getDistance();
				return true;
			}
		}
		return false;
	}

	public int getDistance() {
		return distance;
	}

	@NotNull
	@Override
	protected Token clone() {
		Token token = new OneOf();
		token.setTokenValue(new ArrayList<>(tokenValue));
		return token;
	}

	@NotNull
	@Override
	public String toString() {
		return "OneOf " + super.toString();
	}
}
