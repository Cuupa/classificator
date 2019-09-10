package com.cuupa.classificator.services.kb.semantic.token;

import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Tokens {

	@Nullable
	public static Token get(@NotNull final TokenTextPointer tokenTextPointer) {
		String tokenName = findTokenName(tokenTextPointer);
		List<String> tokenValues = findTokenValue(tokenTextPointer);
		Token token = get(tokenName);
		token.setTokenValue(tokenValues);
		return token;
	}

	@Nullable
	private static Token get(String tokenName) {
		if ("oneOf".equals(tokenName)) {
			return new OneOf();
		} else if ("not".equals(tokenName)) {
			return new Not();
		} else if ("all".equals(tokenName)) {
			return new All();
		} else {
			return null;
		}
	}

	private static String findTokenName(@NotNull final TokenTextPointer pointer) {
		String tokenName = Strings.EMPTY;

		for (int i = pointer.getIndex() - 1; i > 0; i--) {
			if (pointer.get(i) != '{' && pointer.get(i) != ',' && pointer.get(i) > 64 && pointer.get(i) < 123) {
				tokenName = pointer.get(i) + tokenName;
			} else {
				return tokenName;
			}
		}
		return tokenName;
	}

	@NotNull
	private static List<String> findTokenValue(@NotNull final TokenTextPointer pointer) {
		List<String> value = new ArrayList<>();

		StringBuilder tokenValue = new StringBuilder();
		for (int i = pointer.getIndex() + 1; i < pointer.getCharSize(); i++) {
			if (pointer.get(i) == ',') {
				value.add(tokenValue.toString());
				tokenValue = new StringBuilder();
			} else if (pointer.get(i) != '"' && pointer.get(i) != ')') {
				tokenValue.append(pointer.get(i));
			} else if (pointer.get(i) == ')') {
				value.add(tokenValue.toString());
				return value;
			}
		}
		return value;
	}

	private static boolean isNextToken(@NotNull final TokenTextPointer pointer, final int i) {
		return pointer.get(i) == ')' && (pointer.get(i + 1) == ',' || pointer.get(i + 1) == '\r' || pointer.get(i + 1) == '\n');
	}
}
