package com.cuupa.classificator.services.kb.semantic.token;

import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.Extract;
import com.cuupa.classificator.services.kb.semantic.dataExtraction.RegexExtract;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetaDataToken {

	private String name;

    private final List<Token> tokenList = new ArrayList<>();

	public void setName(String name) {
		this.name = name;
	}

	public void addToken(Token token) {
        this.tokenList.add(token);
	}

	public Metadata extract(String text) {
        Map<Metadata, Integer> match = new HashMap<>();

        for (Token token : tokenList) {
            List<String> tokenValue = token.tokenValue;

			for (int i = 0; i < tokenValue.size(); i++) {

				List<Pair<String, String>> compiledText = compileText(text, tokenValue.get(i));
				for (Pair<String, String> pair : compiledText) {
                    // replace the variable with the value
                    token.tokenValue.set(i, pair.getLeft());

                    if (isExclusionCondition(text, token)) {
                        return new Metadata("", "");
                    } else if (token.match(text)) {
						Metadata metadata = new Metadata(name, pair.getRight());
                        match.put(metadata, token.getDistance());
                    }
                }
            }
        }

        return findMostFittingResult(match);
    }

    private Metadata findMostFittingResult(Map<Metadata, Integer> match) {
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

		return new Metadata("", "");
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
            value.add(new ImmutablePair<>(textBeforeToken + matcher.group() + textAfterToken,
                    normalizedValue));
		}

		return value;
	}

    private Extract getExtractForName(String name) {
        if ("[DATE]".equals(name)) {
//            return new DateExtract();
        }

        if ("[IBAN]".equals(name)) {
//            return new IbanExtract();
        }
        
        if(name.startsWith("[REGEX:")) {
        	String string = name.split("REGEX:")[1];
        	return new RegexExtract(string.substring(0, string.length()-1));
        }

        throw new RuntimeException("There is no extract specified");
    }

    private String getTextAfterToken(String var) {
        String[] split = var.split("]");
        if (split.length >= 2) {
            return split[1];
        }
        
        else if(split.length == 1) {
        	return split[0];
        }
        
        else {
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
}
