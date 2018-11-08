package com.cuupa.classificator.services.kb;

import java.util.ArrayList;
import java.util.List;

import com.cuupa.classificator.services.kb.semantic.Topic;
import com.cuupa.classificator.services.kb.semantic.token.InvalidTokenException;
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import com.cuupa.classificator.services.kb.semantic.token.Token;
import com.cuupa.classificator.services.kb.semantic.token.Tokens;

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
				topic.addToken(Tokens.get(new TokenTextPointer(charArray, index)));
			}
			if(charArray[index] == '}') {
				break;
			}
		}

		if (kbFile.contains("$")) {
			MetaDataToken metadata = new MetaDataToken();
			for (int index = 0; index < charArray.length; index++) {
				if (charArray[index] == '$') {
					metadata.setName(findExtractName(charArray, index));
				}

				else if (charArray[index] == '(' && metadata.getName().length() > 0) {
					metadata.addToken(Tokens.get(new TokenTextPointer(charArray, index))); 
					topic.addMetaData(metadata);
					metadata = new MetaDataToken();
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

		if (curlyCloseBrackets != curlyOpenBrackets || normalCloseBrackets != normalOpenBrackets) {
			throw new InvalidTokenException("invalid bracket count");
		}

		if (!kbFile.contains("=")) {
			throw new InvalidTokenException();
		}
	}
}
