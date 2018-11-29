package com.cuupa.classificator.services.kb;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import com.cuupa.classificator.services.kb.semantic.Topic;

public class KnowledgeManager {

	private final List<Topic> topics = new ArrayList<>();

	public KnowledgeManager() {
		initKnowledgeBase();
	}

	private void initKnowledgeBase() {
		File knowledgbaseDir = new File(ApplicationProperties.getKnowledgbaseDir());
		if (knowledgbaseDir.exists() && knowledgbaseDir.isDirectory()) {
			List<File> knowledgeFiles = Arrays.asList(knowledgbaseDir.listFiles());

			knowledgeFiles.stream().forEach(e -> addToTopicList(e));
		}
	}

	private void addToTopicList(File e) {
		 try {
			topics.add(KnowledgeFileParser.parse(FileUtils.readFileToString(e, Charset.defaultCharset())));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public List<SemanticResult> getResults(String text) {
		List<SemanticResult> value = new ArrayList<>();
		
		for (Topic topic : topics) {
			if(topic.match(text)) {
				SemanticResult semanticResult = new SemanticResult(topic.getName());
				semanticResult.setMetaData(topic.getMetaData(text));
				value.add(semanticResult);
			}
		}
		return value;
	}

	public void manualParse(Topic parse) {
		topics.add(parse);
	}
}
