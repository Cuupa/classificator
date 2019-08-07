package com.cuupa.classificator.services.kb.semantic.token;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TokenTextPointer {

	@NotNull
	private final char[] charArray;
	private final int index;

	public TokenTextPointer(@NotNull char[] charArray, int index) {
		this.charArray = Arrays.copyOf(charArray, charArray.length);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public char get(int i) {
		return charArray[i];
	}

	public int getCharSize() {
		return charArray.length;
	}
}
