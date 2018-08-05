package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import java.util.regex.Pattern;

public abstract class Extract {

	private String value;
	
	public String getValue() {
		return value;
	}
	
	protected void setValue(String value) {
		this.value = value;
	}

	public abstract Pattern getPattern();

	public abstract String normalize(String value);
}
