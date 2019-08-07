package com.cuupa.classificator.services.kb.semantic.token;

import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
		return findMostFittingResult(findMetaData(text, createTempList()));
	}

	@NotNull
	private Map<Metadata, Integer> findMetaData(final String text, @NotNull final List<Token> temporaryTokenList) {
		final Map<Metadata, Integer> match = new HashMap<>();
		temporaryTokenList.forEach(getTokenConsumer(text, match));
		return match;
	}

	@NotNull
	private Consumer<Token> getTokenConsumer(String text, @NotNull Map<Metadata, Integer> match) {
		return token -> {
			final List<List<Pair<String, String>>> compiledText = token.tokenValue.stream()
					.map(e -> compileText(text, e)).collect(Collectors.toList());

			if (!compiledText.isEmpty()) {
				List<Token> tokens = replaceCompiledTextInTokenValue(compiledText, cloneTokens(token, compiledText));
				IntStream searchStream = getIntStream(tokens.size());

				if (!searchStream.anyMatch(getPredicateNotTokenMatching(text, token, tokens))) {
					searchStream = getIntStream(tokens.size());

					searchStream.forEach(value -> {
						if (tokens.get(value).match(text)) {
							String metadataValue = compiledText.get(0).get(value).getRight();

							if (!match.entrySet().stream().anyMatch(e -> name.equals(e.getKey().getName())
									&& e.getKey().getValue().equals(metadataValue))) {
								synchronized (MetaDataToken.class) {
									if (!match.entrySet().stream().anyMatch(e -> name.equals(e.getKey().getName())
											&& e.getKey().getValue().equals(metadataValue))) {
										Metadata metadata = new Metadata(name, metadataValue);
										match.put(metadata, tokens.get(value).getDistance());
									}
								}
							}
						}
					});
				}
			}
		};
	}

	@NotNull
	private List<Token> replaceCompiledTextInTokenValue(@NotNull final List<List<Pair<String, String>>> compiledText, @NotNull final List<Token> tokens) {
        IntStream.range(0, compiledText.size()).forEach(i -> IntStream.range(0, tokens.size())
                .forEach(j -> tokens.get(j).tokenValue.set(i, compiledText.get(i).get(j).getLeft())));
        return tokens;
    }

	@NotNull
	private IntStream getIntStream(int size) {
        IntStream searchStream = IntStream.range(0, size);
        if (size > 50) {
            searchStream.parallel();
        }
        return searchStream;
    }

	@NotNull
	private IntPredicate getPredicateNotTokenMatching(String text, Token token, @NotNull List<Token> tokens) {
        return value -> token instanceof Not && tokens.get(value).match(text);
    }

	@NotNull
	private List<Token> cloneTokens(@NotNull Token token, @NotNull List<List<Pair<String, String>>> compiledText) {
		List<Token> tokens = new ArrayList<>();
		IntStream.range(0, compiledText.get(0).size()).forEach(i -> {
			try {
				tokens.add(token.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
		return tokens;
	}

	private List<Token> createTempList() {
		return tokenList.stream().map(e -> {
			try {
				return e.clone();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	private List<Metadata> findMostFittingResult(@NotNull Map<Metadata, Integer> match) {
		Map<Integer, List<Metadata>> entries = new HashMap<>();
		match.entrySet().stream().forEach(entry -> {
			if (entries.containsKey(entry.getValue())) {
				entries.get(entry.getValue()).add(entry.getKey());
			} else {
				ArrayList<Metadata> list = new ArrayList<Metadata>();
				list.add(entry.getKey());
				entries.put(entry.getValue(), list);
			}
		});

		return entries.entrySet().stream().min(Comparator.comparing(Map.Entry::getKey)).map(e -> e.getValue())
				.orElse(new ArrayList<>());
	}

	@NotNull
	private List<Pair<String, String>> compileText(@Nullable String text, @NotNull String tokenValue) {
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

	@NotNull
	private Extract getExtractForName(@NotNull String name) {
		for (Pair<String, String> pair : regexContent) {
			if ("[DATE]".equals(name) && name.contains(pair.getLeft())) {
				return new DateExtract(pair.getRight());
			}

			if ("[IBAN]".equals(name) && name.contains(pair.getLeft())) {
				return new IbanExtract(pair.getRight());
			}

			if ("[SENDER]".equals(name) && name.contains(pair.getLeft())) {
				return new SenderExtract(pair.getRight());
			}

			if (name.contains(pair.getLeft())) {
				return new RegexExtract(pair.getRight());
			}
		}
		throw new RuntimeException("There is no extract specified");
	}

	private String getTextAfterToken(@NotNull String var) {
		String[] split = var.split("]");
		if (split.length >= 2) {
			return split[1];
		} else {
			return Strings.EMPTY;
		}
	}

	private boolean hasVariable(@NotNull String text) {
		return text.contains("[") && text.contains("]");
	}

	public String getName() {
		return name;
	}

	@NotNull
	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setRegexContent(List<Pair<String, String>> regexContent) {
		this.regexContent = regexContent;
	}
}
