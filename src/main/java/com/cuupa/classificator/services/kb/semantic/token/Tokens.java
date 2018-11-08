package com.cuupa.classificator.services.kb.semantic.token;

import java.util.ArrayList;
import java.util.List;

import com.cuupa.classificator.services.kb.TokenTextPointer;

public class Tokens {

	public static Token get(final TokenTextPointer tokenTextPointer) {
		String tokenName = findTokenName(tokenTextPointer);
		List<String> tokenValues = findTokenValue(tokenTextPointer);
		Token token = get(tokenName);
		token.setTokenValue(tokenValues);
		return token;
	}
	
	
	private static Token get(String tokenName) {
		if("oneOf".equals(tokenName)) {
			return new OneOf();
		}
		
		else if("not".equals(tokenName)) {
			return new Not();
		}
		
		else if("all".equals(tokenName)) {
			return new All();
		}
		
		else return null;
	}
	
	private static String findTokenName(final TokenTextPointer pointer) {
		String tokenName = "";

		for (int i = pointer.getIndex() - 1; i > 0; i--) {
			if (pointer.get(i) != '{' && pointer.get(i) != ',' && pointer.get(i) > 64 && pointer.get(i) < 123) {
				tokenName = pointer.get(i) + tokenName;
			} else
				return tokenName;
		}
		return tokenName;
	}
	
	private static List<String> findTokenValue(final TokenTextPointer pointer) {
		List<String> value = new ArrayList<>();

		String tokenValue = "";
		for (int i = pointer.getIndex() + 1; i < pointer.getCharSize(); i++) {
			if (pointer.get(i) == ',') {
				value.add(tokenValue);
				tokenValue = "";
			} else if (pointer.get(i) != '"' && pointer.get(i) != ')') {
				tokenValue = tokenValue + pointer.get(i);
			}

			else if (pointer.get(i) == ')') {
				value.add(tokenValue);
				return value;
			}
		}
		return value;
	}
}
