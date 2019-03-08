package com.cuupa.classificator.services.kb.semantic.token;

import java.util.ArrayList;
import java.util.List;

public class Tokens {

	public static Token get(final TokenTextPointer tokenTextPointer) {
		String tokenName = findTokenName(tokenTextPointer);
		List<String> tokenValues = findTokenValue(tokenTextPointer);
		Token token = get(tokenName);
		token.setTokenValue(tokenValues);
		return token;
	}

	private static Token get(String tokenName) {
		if ("oneOf".equals(tokenName)) {
			return new OneOf();
		}

		else if ("not".equals(tokenName)) {
			return new Not();
		}

		else if ("all".equals(tokenName)) {
			return new All();
		}

		else
			return null;
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

		StringBuilder tokenValue = new StringBuilder();
		for (int i = pointer.getIndex() + 1; i < pointer.getCharSize(); i++) {
			if (pointer.get(i) == ',') {
				value.add(tokenValue.toString());
				tokenValue = new StringBuilder();
			} else if (pointer.get(i) != '"' && pointer.get(i) != ')') {
				tokenValue.append(pointer.get(i));
			}

			else if (pointer.get(i) == ')') {
				value.add(tokenValue.toString());
				return value;
			}
		}
		return value;
	}
	
	private static boolean isNextToken(final TokenTextPointer pointer, final int i) {
		return pointer.get(i) == ')' && (pointer.get(i + 1) == ',' || pointer.get(i+1) == '\r' || pointer.get(i+1) == '\n');
	}
}
