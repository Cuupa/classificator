package com.cuupa.classificator.services.kb.semantic.token;

import java.util.Arrays;

public class TokenTextPointer {

	private final char[] charArray;
	private final int index;
	
	public TokenTextPointer(char[] charArray, int index) {
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
