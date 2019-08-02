package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import com.cuupa.classificator.services.kb.semantic.Topic;
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeManager {

    private static final Log LOG = LogFactory.getLog(KnowledgeManager.class);

    private final List<Topic> topics = new ArrayList<>();

    private ApplicationProperties applicationProperties;

    public KnowledgeManager(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        initKnowledgeBase();
    }

    public void reloadKB() {
        topics.clear();
        initKnowledgeBase();
    }

    private void initKnowledgeBase() {
        try {
            File knowledgebaseDir = ResourceUtils.getFile("classpath:" + applicationProperties.getKnowledgbaseDir());
            if (knowledgebaseDir.exists() && knowledgebaseDir.isDirectory()) {
                File[] files = knowledgebaseDir.listFiles();

                if (files == null) {
                    return;
                }

                Optional<File> regexList = Arrays.stream(files).filter(e -> e.getName().equals("regex")).findFirst();
                final List<Pair<String, String>> regexContent = new ArrayList<>();
                regexList.ifPresent(file -> regexContent.addAll(Arrays.stream(Objects.requireNonNull(file.listFiles()))
                        .filter(e -> e.getName().endsWith(".regx")).map(this::createRegex).collect(Collectors.toList())));

                final List<MetaDataToken> metaDataTokenList = getMetaData(files);

                List<Topic> topicList = Arrays.stream(files).filter(e -> e.getName().endsWith(".dsl"))
                        .map(this::createTopic).collect(Collectors.toList());

                topicList.stream().forEach(topic -> topic.addMetaDataList(metaDataTokenList));
                topicList.stream()
                        .forEach(topic -> topic.getMetaDataList().forEach(token -> token.setRegexContent(regexContent)));
                topics.addAll(topicList);
            }
        } catch (FileNotFoundException fnfe) {
            LOG.error("Error loading files", fnfe);
        }
    }

    private List<MetaDataToken> getMetaData(File[] files) {
        Optional<File> metadataDir = Arrays.stream(files).filter(e -> e.getName().equals("metadata")).findFirst();
        return metadataDir.map(file -> Arrays.stream(Objects.requireNonNull(file.listFiles())).filter(e -> e.getName().endsWith(".meta"))
                .map(this::createMetaData).collect(Collectors.toList())).orElseGet(() -> new ArrayList<>(0));
    }

    private MetaDataToken createMetaData(File metaFile) {
        try {
            return KnowledgeFileParser.parseMetaFile(FileUtils.readFileToString(metaFile, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private Pair<String, String> createRegex(File regexFile) {
        try {
            return KnowledgeFileParser.parseRegexFile(regexFile.getName(),
                    FileUtils.readFileToString(regexFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Topic createTopic(File kbFile) {
        try {
            return KnowledgeFileParser.parseTopicFile(FileUtils.readFileToString(kbFile, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public List<SemanticResult> getResults(String text) {
        List<SemanticResult> foundTopics = topics.stream().filter(e -> e.match(text))
                .map(e -> new SemanticResult(e.getName(), e.getMetaData(text))).collect(Collectors.toList());

        if (foundTopics.isEmpty()) {
            SemanticResult other = topics.stream().filter(e -> !e.match(text))
                    .map(e -> new SemanticResult("OTHER", e.getMetaData(text))).collect(Collectors.toList()).stream()
                    .filter(e -> e.getMetaData().size() > 0).findFirst().orElse(new SemanticResult("OTHER", new ArrayList<>(0)));
            foundTopics.add(other);
        }

        return foundTopics;
    }

    public void manualParse(Topic parse) {
        topics.add(parse);
    }
}
