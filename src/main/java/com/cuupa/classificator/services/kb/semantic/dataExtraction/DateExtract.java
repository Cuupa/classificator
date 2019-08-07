package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class DateExtract extends Extract {

	private static Pattern pattern;

	public DateExtract(@NotNull String regex) {
		pattern = Pattern.compile(regex);
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}

	@NotNull
	@Override
	public String normalize(@NotNull String value) {
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
