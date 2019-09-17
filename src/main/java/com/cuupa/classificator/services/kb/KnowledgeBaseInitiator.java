package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.services.kb.semantic.SenderToken;
import com.cuupa.classificator.services.kb.semantic.Topic;
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeBaseInitiator {

    private final Log LOG = LogFactory.getLog(KnowledgeBaseInitiator.class);

    private final ApplicationProperties applicationProperties;

    public KnowledgeBaseInitiator(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public KnowledgeBase initKnowledgeBase() {
        KnowledgeBase kb = new KnowledgeBase();
        try {
            File knowledgebaseDir = ResourceUtils.getFile(applicationProperties.getKnowledgbaseDir());
            if (!knowledgebaseDir.isDirectory()) {
                return kb;
            }

            File[] files = knowledgebaseDir.listFiles();

            if (files == null) {
                return kb;
            }

            Optional<File> regexList = Arrays.stream(files).filter(e -> e.getName().equals("regex")).findFirst();
            final List<Pair<String, String>> regexContent = new ArrayList<>();
            regexList.ifPresent(file -> regexContent.addAll(Arrays.stream(Objects.requireNonNull(file.listFiles()))
                                                                  .filter(e -> e.getName().endsWith(".regx"))
                                                                  .map(this::createRegex)
                                                                  .collect(Collectors.toList())));

            final List<MetaDataToken> metaDataTokenList = getMetaData(files);

            List<Topic> topicList = Arrays.stream(files)
                                          .filter(e -> e.getName().endsWith(".dsl"))
                                          .map(this::createTopic)
                                          .collect(Collectors.toList());

            topicList.forEach(topic -> topic.addMetaDataList(metaDataTokenList));
            topicList.forEach(topic -> topic.getMetaDataList().forEach(token -> token.setRegexContent(regexContent)));
            kb.setTopics(topicList);
            kb.setSenders(getSenders(applicationProperties.getSenderFiles()));
        } catch (FileNotFoundException fnfe) {
            LOG.error("Error loading files", fnfe);
        }
        return kb;
    }

    private List<MetaDataToken> getMetaData(@NotNull File[] files) {
        Optional<File> metadataDir = Arrays.stream(files).filter(e -> e.getName().equals("metadata")).findFirst();
        return metadataDir.map(file -> Arrays.stream(Objects.requireNonNull(file.listFiles()))
                                             .filter(e -> e.getName().endsWith(".meta"))
                                             .map(this::createMetaData)
                                             .collect(Collectors.toList())).orElseGet(() -> new ArrayList<>(0));
    }

    @Nullable
    private MetaDataToken createMetaData(@NotNull File metaFile) {
        try {
            return KnowledgeFileParser.parseMetaFile(FileUtils.readFileToString(metaFile, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Nullable
    private Pair<String, String> createRegex(@NotNull File regexFile) {
        try {
            return KnowledgeFileParser.parseRegexFile(regexFile.getName(),
                                                      FileUtils.readFileToString(regexFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private Topic createTopic(@NotNull File kbFile) {
        try {
            return KnowledgeFileParser.parseTopicFile(FileUtils.readFileToString(kbFile, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    @Nullable
    private SenderToken createSenderTokens(@NotNull File file) {
        try {
            return KnowledgeFileParser.parseSenderFile(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
        } catch (IOException ioe) {
            LOG.error("Unable to get sendertokens", ioe);
        }
        return null;
    }

    private List<SenderToken> getSenders(@NotNull String senderFolderString) {
        List<SenderToken> senderTokens = new ArrayList<>();
        try {
            File senderFolder = ResourceUtils.getFile(senderFolderString);
            File[] senderFiles = senderFolder.listFiles();
            if (senderFiles == null) {
                return senderTokens;
            }
            senderTokens = Arrays.stream(senderFiles).map(this::createSenderTokens).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            LOG.error("Unable to get senders", e);
        }
        return senderTokens;
    }
}
