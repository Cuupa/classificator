package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import java.util.regex.Pattern;

public class IbanExtract extends Extract {

	private static final String regex = "[A-Z]{2}[0-9]{2}" + "[\\s]?[0-9]{4}" + "[\\s]?[0-9]{4}" + "[\\s]?[0-9]{4}"
			+ "[\\s]?[0-9]{4}" + "[\\s]?[0-9]{2}";

	private static final Pattern pattern = Pattern.compile(regex);

	@Override
	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public String normalize(String value) {
		value = value.replace(" ", "");
		StringBuilder sb = new StringBuilder();
		char[] charArray = value.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (i % 4 == 0 && i > 0) {
				sb.append(" ");
			}
			sb.append(charArray[i]);

		}

		return sb.toString();
	}
}
