package com.cuupa.classificator.services.kb.semantic.token;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Token {

	List<String> tokenValue;

	public abstract boolean match(String text);

	public List<String> getTokenValue() {
		return tokenValue;
	}

	void setTokenValue(List<String> tokenValue) {
		this.tokenValue = tokenValue;
	}

	public abstract int getDistance();

	@NotNull
	@Override
	protected abstract Token clone();

	@Override
	public String toString() {
		return String.join(",", tokenValue);
	}
}
