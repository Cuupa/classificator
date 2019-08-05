package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.configuration.application.ApplicationProperties;
import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.SenderToken;
import com.cuupa.classificator.services.kb.semantic.Topic;
import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
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

    private final List<SenderToken> senderTokens = new ArrayList<>();

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
            File knowledgebaseDir = ResourceUtils.getFile(applicationProperties.getKnowledgbaseDir());
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

                topicList.forEach(topic -> topic.addMetaDataList(metaDataTokenList));
                topicList
                        .forEach(topic -> topic.getMetaDataList().forEach(token -> token.setRegexContent(regexContent)));
                topics.addAll(topicList);

                senderTokens.addAll(getSenders(applicationProperties.getSenderFiles()));
            }
        } catch (FileNotFoundException fnfe) {
            LOG.error("Error loading files", fnfe);
        }
    }

    private List<SenderToken> getSenders(String senderFolderString) {
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

    @Nullable
    private SenderToken createSenderTokens(File file) {
        try {
            return KnowledgeFileParser.parseSenderFile(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
        } catch (IOException ioe) {
            LOG.error("Unable to get sendertokens", ioe);
        }
        return null;
    }

    private List<MetaDataToken> getMetaData(File[] files) {
        Optional<File> metadataDir = Arrays.stream(files).filter(e -> e.getName().equals("metadata")).findFirst();
        return metadataDir.map(file -> Arrays.stream(Objects.requireNonNull(file.listFiles())).filter(e -> e.getName().endsWith(".meta"))
                .map(this::createMetaData).collect(Collectors.toList())).orElseGet(() -> new ArrayList<>(0));
    }

    @Nullable
    private MetaDataToken createMetaData(File metaFile) {
        try {
            return KnowledgeFileParser.parseMetaFile(FileUtils.readFileToString(metaFile, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Nullable
    private Pair<String, String> createRegex(File regexFile) {
        try {
            return KnowledgeFileParser.parseRegexFile(regexFile.getName(),
                    FileUtils.readFileToString(regexFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private Topic createTopic(File kbFile) {
        try {
            return KnowledgeFileParser.parseTopicFile(FileUtils.readFileToString(kbFile, StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public List<SemanticResult> getResults(final String text) {
        List<SemanticResult> foundTopics = topics.stream().filter(e -> e.match(text))
                .map(e -> new SemanticResult(e.getName(), e.getMetaData(text))).collect(Collectors.toList());

        if (foundTopics.isEmpty()) {
            SemanticResult other = topics.stream().filter(e -> !e.match(text))
                    .map(e -> new SemanticResult(Topic.OTHER, e.getMetaData(text))).collect(Collectors.toList()).stream()
                    .filter(e -> e.getMetaData().size() > 0).findFirst().orElse(new SemanticResult(Topic.OTHER, new ArrayList<>(0)));
            foundTopics.add(other);
        }

        List<SenderToken> sendersFoundInText = this.senderTokens.stream().filter(e -> e.match(text)).collect(Collectors.toList());
        String sender = validateSender(foundTopics, sendersFoundInText);
        foundTopics.forEach(e -> e.setSender(sender));
        return foundTopics;
    }

    private String validateSender(final List<SemanticResult> foundTopics, final List<SenderToken> senderTokens) {
        final List<Metadata> sendersFromTopic = new ArrayList<>();

        for (SemanticResult result : foundTopics) {
            sendersFromTopic.addAll(result.getMetaData().stream().filter(e -> "sender".equals(e.getName())).collect(Collectors.toList()));
        }

        if (sendersFromTopic.size() > 1) {
            return sendersFromTopic.stream().map(e -> new SenderToken(e.getValue())).filter(senderTokens::contains).findFirst().orElse(new SenderToken(SenderToken.UNKNOWN)).getName();
        } else if (sendersFromTopic.size() == 0 && senderTokens.size() == 1) {
            return senderTokens.get(0).getName();
        }
        return SenderToken.UNKNOWN;
    }

    public void manualParse(Topic parse) {
        topics.add(parse);
    }
}
