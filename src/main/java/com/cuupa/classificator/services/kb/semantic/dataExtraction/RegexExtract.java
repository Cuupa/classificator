package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class RegexExtract extends Extract {

	@NotNull
	private final Pattern pattern;

	public RegexExtract(@NotNull final String regex) {
		pattern = Pattern.compile(regex);
	}

	@NotNull
	@Override
	public Pattern getPattern() {
		return pattern;
	}

	@NotNull
	@Override
	public String normalize(@NotNull String value) {
		return value.replace(" ", Strings.EMPTY);
	}
}
