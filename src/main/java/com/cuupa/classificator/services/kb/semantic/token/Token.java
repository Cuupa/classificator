package com.cuupa.classificator.services.kb.semantic.token;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Token {
	
	protected List<String> tokenValue;

	public abstract boolean match(String text);

	public void setTokenValue(List<String> tokenValue) {
		this.tokenValue = tokenValue;
	}
	
	public List<String> getTokenValue() {
		return tokenValue;
	}

	public abstract int getDistance();

	@NotNull
	@Override
	protected abstract Token clone() throws CloneNotSupportedException;
	
	@Override
	public String toString() {
		return tokenValue.stream().collect(Collectors.joining(","));
	}
}
