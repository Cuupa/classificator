package com.cuupa.classificator.services.kb.semantic;

public class InvalidTokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String string) {
		super(string);
	}

	public InvalidTokenException() {
	}
}
