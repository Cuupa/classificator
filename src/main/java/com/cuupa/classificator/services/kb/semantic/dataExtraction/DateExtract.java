package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import java.util.regex.Pattern;

public class DateExtract extends Extract {

	private static final String REGEX = "([0-3])?[0-9]\\.([0-1])?[0-9]\\.([0-9][0-9])?[0-9][0-9]";

	private static final Pattern pattern = Pattern.compile(REGEX);

	@Override
	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public String normalize(String value) {
		String[] split = value.split("\\.");
		String day = split[0];
		String month = split[1];
		String year = split[2];
		
		if(day.length() == 1) {
			day = "0" + day;
		}
		
		if(month.length() == 1) {
			month = "0"+ month;
		}
		
		if(year.length() == 2) {
			year = "20" + year;
		}
		return day + "." + month + "." + year;
	}
	
}
