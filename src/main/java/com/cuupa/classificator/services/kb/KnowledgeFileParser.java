package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.services.kb.semantic.SenderToken;
import com.cuupa.classificator.services.kb.semantic.Topic;
import com.cuupa.classificator.services.kb.semantic.token.InvalidTokenException;
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import com.cuupa.classificator.services.kb.semantic.token.TokenTextPointer;
import com.cuupa.classificator.services.kb.semantic.token.Tokens;
import org.apache.commons.lang3.tuple.Pair;

public class KnowledgeFileParser {

    static Topic parseTopicFile(String kbFile) {
        validateToken(kbFile);
        return parseTopic(kbFile);
    }

    static MetaDataToken parseMetaFile(String kbFile) {
        validateToken(kbFile);
        return parseMetaData(kbFile);
    }

    static SenderToken parseSenderFile(String kbFile) {
        validateToken(kbFile);
        return parseSender(kbFile);
    }

    private static SenderToken parseSender(String kbFile) {
        String[] split = kbFile.split("=");
        String topicName = split[0].trim();

        SenderToken senderToken = new SenderToken();
        senderToken.setName(topicName);

        char[] charArray = kbFile.toCharArray();
        for (int index = 0; index < charArray.length; index++) {
            if (charArray[index] == '(') {
                senderToken.addToken(Tokens.get(new TokenTextPointer(charArray, index)));
            }
            if (charArray[index] == '}') {
                break;
            }
        }
        return senderToken;
    }

    static Pair<String, String> parseRegexFile(String filename, String content) {
        return Pair.of(filename.split("\\.")[0], content);
    }

    private static MetaDataToken parseMetaData(String kbFile) {
        MetaDataToken metadata = new MetaDataToken();
        char[] charArray = kbFile.toCharArray();
        for (int index = 0; index < charArray.length; index++) {
            if (charArray[index] == '$') {
                metadata.setName(findExtractName(charArray, index));
            } else if (charArray[index] == '(' && metadata.getName() != null && metadata.getName().length() > 0) {
                metadata.addToken(Tokens.get(new TokenTextPointer(charArray, index)));
            }
        }

        return metadata;
    }

    public static Topic parseTopic(String kbFile) {
        String[] split = kbFile.split("=");
        String topicName = split[0].trim();

        Topic topic = new Topic();
        topic.setName(topicName);

        char[] charArray = kbFile.toCharArray();
        for (int index = 0; index < charArray.length; index++) {
            if (charArray[index] == '(') {
                topic.addToken(Tokens.get(new TokenTextPointer(charArray, index)));
            }
            if (charArray[index] == '}') {
                break;
            }
        }

        // these are the metadata which is only applicable for the specific topic 
        if (kbFile.contains("$")) {
            MetaDataToken metadata = new MetaDataToken();
            for (int index = 0; index < charArray.length; index++) {
                if (charArray[index] == '$') {
                    metadata.setName(findExtractName(charArray, index));
                } else if (charArray[index] == '(' && metadata.getName() != null && metadata.getName().length() > 0) {
                    metadata.addToken(Tokens.get(new TokenTextPointer(charArray, index)));
                    topic.addMetaData(metadata);
                    metadata = new MetaDataToken();
                }
            }
        }

        return topic;
    }

    private static String findExtractName(final char[] charArray, int index) {
        StringBuilder extractName = new StringBuilder();
        for (int i = index; i < charArray.length; i++) {
            if (charArray[i] == '=') {
                for (int j = i - 1; j > 0; j--) {
                    if (charArray[j] == '$') {
                        return extractName.toString().trim();
                    }
                    extractName.insert(0, charArray[j]);
                }
            }
        }

        return extractName.toString().trim();
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
