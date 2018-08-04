package com.cuupa.classificator.services.kb;

import java.util.ArrayList;
import java.util.List;

import com.cuupa.classificator.services.kb.semantic.InvalidTokenException;
import com.cuupa.classificator.services.kb.semantic.Topic;

public class KnowledgeFileParser {

	public static Topic parse(String kbFile) {
		validateToken(kbFile);
		return parseFile(kbFile);
	}

	private static Topic parseFile(String kbFile) {
		String[] split = kbFile.split("=");
		String topicName = split[0];

		Topic topic = new Topic();
		topic.setName(topicName);

		char[] charArray = kbFile.toCharArray();
		for (int index = 0; index < charArray.length; index++) {
			if (charArray[index] == '(') {
				String tokenName = findTokenName(charArray, index);
				Token token = TokenUtil.get(tokenName);
				token.setTokenValue(findTokenValue(charArray, index));
				topic.addToken(token);
			}
			if(charArray[index] == '}') {
				break;
			}
		}

		if (kbFile.contains("$")) {
			String extractName = "";
			MetaData metadata = new MetaData();
			for (int index = 0; index < charArray.length; index++) {
				if (charArray[index] == '$') {
					extractName = findExtractName(charArray, index);
					metadata.setName(extractName);
				}

				else if (charArray[index] == '(' && extractName.length() > 0) {
					Token token = TokenUtil.get(findTokenName(charArray, index)); 
					List<String> tokenValue = findTokenValue(charArray, index);
					token.setTokenValue(tokenValue);
					metadata.addToken(token);
					topic.addMetaData(metadata);
				}
			}
		}

		return topic;
	}

	private static String findExtractName(final char[] charArray, int index) {
		String extractName = "";
		for (int i = index; i < charArray.length; i++) {
			if (charArray[i] == '=') {
				for (int j = i- 1; j > 0; j--) {
					if(charArray[j] == '$') {
						return extractName.trim();
					}
					extractName = charArray[j] + extractName;
				}
			}
		}

		return extractName.trim();
	}

	private static List<String> findTokenValue(final char[] charArray, int index) {
		List<String> value = new ArrayList<>();

		String tokenValue = "";
		for (int i = index + 1; i < charArray.length; i++) {
			if (charArray[i] == ',') {
				value.add(tokenValue);
				tokenValue = "";
			} else if (charArray[i] != '"' && charArray[i] != ')') {
				tokenValue = tokenValue + charArray[i];
			}

			else if (charArray[i] == ')') {
				value.add(tokenValue);
				return value;
			}
		}
		return value;
	}

	private static String resolveVars(String tokenValue) {
		char[] charArray = tokenValue.toCharArray();
		String var = "";

		for (int index = 0; index < charArray.length; index++) {
			if (charArray[index] == '[') {
				for (int i = index + 1; i < charArray.length; i++) {
					if(charArray[i] == ']') {
						break;
					}
					var = var + charArray[i];
				}
			}
		}
		return var;
	}

	private static String findTokenName(final char[] charArray, int index) {
		String tokenName = "";

		for (int i = index - 1; i > 0; i--) {
			if (charArray[i] != '{' && charArray[i] != ',' && charArray[i] > 64 && charArray[i] < 123) {
				tokenName = charArray[i] + tokenName;
			} else
				return tokenName;
		}
		return tokenName;
	}

	private static void validateToken(String kbFile) {
		char[] charArray = kbFile.toCharArray();

		int curlyOpenBrackets = 0, curlyCloseBrackets = 0;
		int normalOpenBrackets = 0, normalCloseBrackets = 0;
		for (char c : charArray) {
			if (c == '{') {
				curlyOpenBrackets++;
			} else if (c == '}') {
				curlyCloseBrackets++;
			} else if (c == '(') {
				normalOpenBrackets++;
			} else if (c == ')') {
				normalCloseBrackets++;
			}
		}

		if (curlyCloseBrackets != curlyOpenBrackets && normalCloseBrackets != normalOpenBrackets) {
			throw new InvalidTokenException("invalid bracket count");
		}

		if (!kbFile.contains("=")) {
			throw new InvalidTokenException();
		}
	}
}
