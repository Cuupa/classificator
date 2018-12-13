package com.cuupa.classificator.services.kb.semantic.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.DateExtract;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.Extract;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.IbanExtract;

public class MetaDataToken {

	private String name;

	private List<Token> token = new ArrayList<>();

	public void setName(String name) {
		this.name = name;
	}

	public void addToken(Token token) {
		this.token.add(token);
	}

	public Metadata extract(String text) {
		for (Token token2 : token) {
			List<String> tokenValue = token2.tokenValue;

			Map<Metadata, Integer> match = new HashMap<>();
			for (int i = 0; i < tokenValue.size(); i++) {

				List<Pair<String, String>> compiledText = compileText(text, tokenValue.get(i));
				for (Pair<String, String> pair : compiledText) {
					token2.tokenValue.set(i, pair.getLeft());
					if (token2.match(text)) {
						Metadata metadata = new Metadata(name, pair.getRight());
						match.put(metadata, token2.getDistance());
					}
				}
			}

			Set<Entry<Metadata, Integer>> entrySet = match.entrySet();
			Entry<Metadata, Integer> smallestDistance = null;

			for (Entry<Metadata, Integer> entry : entrySet) {
				if (smallestDistance == null) {
					smallestDistance = entry;
				}

				if (smallestDistance.getValue() > entry.getValue()) {
					smallestDistance = entry;
				}
			}

			if (smallestDistance != null) {
				return smallestDistance.getKey();
			}
		}
		return null;
	}

	private List<Pair<String, String>> compileText(String text, String tokenValue) {
		List<Pair<String, String>> value = new ArrayList<>();
		if (text == null) {
			value.add(new ImmutablePair<String, String>(tokenValue, ""));
			return value;
		}

		if (hasVariable(tokenValue)) {
			String[] split = tokenValue.split("\\[");
			String textBeforeToken = split[0];
			String var = "[" + split[1];
			String textAfterToken = getTextAfterToken(var);
			var = var.split("]")[0] + "]";
			// textAferToken =
			Extract extract = compile(var);
			Pattern pattern = extract.getPattern();
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String normalizedValue = extract.normalize(matcher.group());
				value.add(new ImmutablePair<String, String>(textBeforeToken + matcher.group() + textAfterToken,
						normalizedValue));
			}
		}

		return value;
	}

	private String getTextAfterToken(String var) {
		String[] split = var.split("]");
		if (split.length >= 2) {
			return split[1];
		} else {
			return "";
		}
	}

	private Extract compile(String var) {
		if ("[DATE]".equals(var)) {
			return new DateExtract();
		}

		if ("[IBAN]".equals(var)) {
			return new IbanExtract();
		}

		return null;
	}

	private boolean hasVariable(String text) {
		return text.contains("[") && text.contains("]");
	}

	public String getName() {
		return name;
	}
}
