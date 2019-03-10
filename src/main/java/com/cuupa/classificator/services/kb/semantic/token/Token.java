package com.cuupa.classificator.services.kb.semantic.token;

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

	@Override
	protected abstract Token clone();
	
	@Override
	public String toString() {
		return tokenValue.stream().collect(Collectors.joining(","));
	}
}
