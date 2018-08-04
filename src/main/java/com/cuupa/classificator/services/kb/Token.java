package com.cuupa.classificator.services.kb;

import java.util.List;

public abstract class Token {
	
	protected List<String> tokenValue;

	public abstract boolean match(String text);

	public void setTokenValue(List<String> tokenValue) {
		this.tokenValue = tokenValue;
	}
}
