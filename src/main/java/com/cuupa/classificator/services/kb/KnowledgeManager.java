package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import com.cuupa.classificator.services.kb.semantic.Topic;
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import com.cuupa.classificator.services.kb.semantic.token.Token;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
            
            
            Optional<File> regexList = Arrays.stream(files).filter(e -> e.getName().equals("regex")).findFirst();
            final List<Pair<String, String>> regexContent = new ArrayList<>();
            if(regexList.isPresent()) {
            	 regexContent.addAll(Arrays.stream(regexList.get().listFiles()).filter(e -> e.getName().endsWith(".regx")).map(this::createRegex).collect(Collectors.toList()));
            }
            
            final List<MetaDataToken> metaDataTokenList = getMetaData(files);
            
            List<Topic> topicList = Arrays.stream(files)
                    .filter(e -> e.getName().endsWith(".dsl"))
                    .map(this::createTopic).collect(Collectors.toList());
           
            	
//            	for (MetaDataToken token : metaDataTokenList) {
//            		for (Pair<String, String> pair : collect) {
//            			for (Token t : token.getTokenList()) {
//            				for(int i = 0; i < t.getTokenValue().size(); i++) {
//								t.getTokenValue().set(i, t.getTokenValue().get(i).replace("[" + pair.getLeft() + "]", pair.getRight()));
//							}
//							System.out.println(t);
//						}
//					}
//				}
//            	
            topicList.stream().forEach(topic -> topic.addMetaDataList(metaDataTokenList));
            topicList.stream().forEach(topic -> topic.getMetaDataList().forEach(token -> token.setRegexContent(regexContent)));
            topics.addAll(topicList);
		}
	}

	private List<MetaDataToken> getMetaData(File[] files) {
		Optional<File> metadataDir = Arrays.stream(files).filter(e -> e.getName().equals("metadata")).findFirst();
		if(metadataDir.isPresent()) {
			return Arrays.stream(metadataDir.get().listFiles())
		            .filter(e -> e.getName().endsWith(".meta"))
		            .map(this::createMetaData).collect(Collectors.toList());
		}
		return new ArrayList<>(0);
	}

    private MetaDataToken createMetaData(File metaFile) {
        try {
        	return KnowledgeFileParser.parseMetaFile(FileUtils.readFileToString(metaFile, Charset.defaultCharset()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    
    private Pair<String, String> createRegex(File regexFile) {
    	try {
			return KnowledgeFileParser.parseRegexFile(regexFile.getName(), FileUtils.readFileToString(regexFile, Charset.defaultCharset()));
		} catch (IOException e) {
			e.printStackTrace();
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
        return topics.stream()
				.filter(e -> e.match(text))
				.map(e -> new SemanticResult(e.getName(), e.getMetaData(text)))
				.collect(Collectors.toList());
	}

	public void manualParse(Topic parse) {
		topics.add(parse);
	}
}
