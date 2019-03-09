package com.cuupa.classificator.services.kb.semantic.token;

import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.DateExtract;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.Extract;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.IbanExtract;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.RegexExtract;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetaDataToken {

	private String name;

	private final List<Token> tokenList = new ArrayList<>();

	private List<Pair<String, String>> regexContent;

	public void setName(String name) {
		this.name = name;
	}

	public void addToken(Token token) {
		this.tokenList.add(token);
	}

	public List<Metadata> extract(String text) {
		List<Token> temporaryTokenList = createTempList();
		Map<Metadata, Integer> match = findMetaData(text, temporaryTokenList);
		return findMostFittingResult(match);
	}

	private Map<Metadata, Integer> findMetaData(String text, List<Token> temporaryTokenList) {
		Map<Metadata, Integer> match = new HashMap<>();

		temporaryTokenList.parallelStream().forEach(new Consumer<Token>() {

			@Override
			public void accept(Token token) {
				List<String> tokenValue = token.tokenValue;

				for (int i = 0; i < tokenValue.size(); i++) {

					List<Pair<String, String>> compiledText = compileText(text, tokenValue.get(i));
					for (Pair<String, String> pair : compiledText) {
						// replace the variable with the value
						token.tokenValue.set(i, pair.getLeft());

						if (isExclusionCondition(text, token)) {
						} else if (token.match(text)) {
							Metadata metadata = new Metadata(name, pair.getRight());
							match.put(metadata, token.getDistance());
						}
					}
				}
			}
		});
		return match;
	}

	private List<Token> createTempList() {
		List<Token> temporaryTokenList = new ArrayList<Token>();

		for (Token token : tokenList) {
			temporaryTokenList.add(token.clone());
		}

		return temporaryTokenList;
	}

	private List<Metadata> findMostFittingResult(Map<Metadata, Integer> match) {

		Map<Integer, List<Metadata>> entries = new HashMap<>();
		match.entrySet().stream().forEach(new Consumer<Entry<Metadata, Integer>>() {

			@Override
			public void accept(Entry<Metadata, Integer> entry) {
				if (entries.containsKey(entry.getValue())) {
					entries.get(entry.getValue()).add(entry.getKey());
				} else {
					ArrayList<Metadata> list = new ArrayList<Metadata>();
					list.add(entry.getKey());
					entries.put(entry.getValue(), list);
				}
			}

		});
		
		List<Metadata> result = entries.entrySet().stream().min(new Comparator<Entry<Integer, List<Metadata>>>() {

			@Override
			public int compare(Entry<Integer, List<Metadata>> o1, Entry<Integer, List<Metadata>> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
			
		}).map(e-> e.getValue()).orElse(new ArrayList<Metadata>());
		
		return result;
	}

	private boolean isExclusionCondition(String text, Token token) {
		return token instanceof Not && token.match(text);
	}

	private List<Pair<String, String>> compileText(String text, String tokenValue) {
		List<Pair<String, String>> value = new ArrayList<>();
		if (text == null || !hasVariable(tokenValue)) {
			value.add(new ImmutablePair<>(tokenValue, tokenValue));
			return value;
		}

		String[] split = tokenValue.split("\\[");
		String textBeforeToken = split[0];
		String variable = "[" + split[1];
		String textAfterToken = getTextAfterToken(variable);
		variable = variable.split("]")[0] + "]";
		Extract extract = getExtractForName(variable);
		Pattern pattern = extract.getPattern();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String normalizedValue = extract.normalize(matcher.group());
			value.add(new ImmutablePair<>(textBeforeToken + normalizedValue + textAfterToken, normalizedValue));
		}

		return value;
	}

	private Extract getExtractForName(String name) {

		for (Pair<String, String> pair : regexContent) {
			if ("[DATE]".equals(name) && name.contains(pair.getLeft())) {
				return new DateExtract(pair.getRight());
			}

			if ("[IBAN]".equals(name) && name.contains(pair.getLeft())) {
				return new IbanExtract(pair.getRight());
			}

			if (name.contains(pair.getLeft())) {
				return new RegexExtract(pair.getRight());
			}
		}
		throw new RuntimeException("There is no extract specified");
	}

	private String getTextAfterToken(String var) {
		String[] split = var.split("]");
		if (split.length >= 2) {
			return split[1];
		} else {
			return Strings.EMPTY;
		}
	}

	private boolean hasVariable(String text) {
		return text.contains("[") && text.contains("]");
	}

	public String getName() {
		return name;
	}

	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setRegexContent(List<Pair<String, String>> regexContent) {
		this.regexContent = regexContent;
	}
}
