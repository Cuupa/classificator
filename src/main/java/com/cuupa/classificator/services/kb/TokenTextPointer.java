package com.cuupa.classificator.services.kb;

import java.util.Arrays;

public class TokenTextPointer {

	private char[] charArray;
	private int index;
	
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
