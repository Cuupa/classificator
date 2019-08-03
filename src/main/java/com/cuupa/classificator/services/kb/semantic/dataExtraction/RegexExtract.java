package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import java.util.regex.Pattern;

public class RegexExtract extends Extract {

	private final Pattern pattern;
	
	public RegexExtract(final String regex) {
		pattern = Pattern.compile(regex);
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public String normalize(String value) {
        return value.replace(" ", "");
	}
}
