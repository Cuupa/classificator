package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import com.cuupa.classificator.services.kb.semantic.Topic;
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KnowledgeManager {

	private final List<Topic> topics = new ArrayList<>();

	public KnowledgeManager() {
		initKnowledgeBase();
	}

	private void initKnowledgeBase() {
        File knowledgebaseDir = new File(ApplicationProperties.getKnowledgbaseDir());
        if (knowledgebaseDir.exists() && knowledgebaseDir.isDirectory()) {
            File[] files = knowledgebaseDir.listFiles();
            if (files == null) {
                return;
            }
            List<MetaDataToken> metaDataTokenList = Arrays.stream(files)
                    .filter(e -> e.getName().endsWith(".meta"))
                    .map(this::createMetaData).collect(Collectors.toList());

            List<Topic> topicList = Arrays.stream(files)
                    .filter(e -> e.getName().endsWith(".dsl"))
                    .map(this::createTopic).collect(Collectors.toList());

            topicList.stream().forEach(topic -> topic.setMetaDataList(metaDataTokenList));
            topics.addAll(topicList);
		}
	}

    private MetaDataToken createMetaData(File metaFile) {
        try {
            return KnowledgeFileParser.parseMetaFile(FileUtils.readFileToString(metaFile, Charset.defaultCharset()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    private Topic createTopic(File kbFile) {
		try {
            return KnowledgeFileParser.parseTopicFile(FileUtils.readFileToString(kbFile, Charset.defaultCharset()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        return null;
	}

	public List<SemanticResult> getResults(String text) {
        return topics.parallelStream()
				.filter(e -> e.match(text))
				.map(e -> new SemanticResult(e.getName(), e.getMetaData(text)))
				.collect(Collectors.toList());
	}

	public void manualParse(Topic parse) {
		topics.add(parse);
	}
}
