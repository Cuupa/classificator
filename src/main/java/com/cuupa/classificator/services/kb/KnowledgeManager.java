package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.SenderToken;
import com.cuupa.classificator.services.kb.semantic.Topic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KnowledgeManager {

    private static final Log LOG = LogFactory.getLog(KnowledgeManager.class);

    private KnowledgeBase knowledgeBase;

    private KnowledgeBaseInitiator knowledgeBaseInitiator;

    public KnowledgeManager(KnowledgeBaseInitiator knowledgeBaseInitiator) {
        this.knowledgeBaseInitiator = knowledgeBaseInitiator;
        knowledgeBase = this.knowledgeBaseInitiator.initKnowledgeBase();
    }

    public void reloadKB() {
        knowledgeBase.clear();
        knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase();
    }

    public List<SemanticResult> getResults(final String text) {
        List<SemanticResult> matchingSemanticResults = getTopicsWithMetadata(text);

        if (matchingSemanticResults.isEmpty()) {
            LOG.debug("Found no matching Topics");
            SemanticResult other = getMetadatasForTopicOther(text);
            matchingSemanticResults.add(other);
        }

        List<SenderToken> sendersFromTokenFoundInText = getSendersFromKnowledgebase(text);

        final String sender = getSender(matchingSemanticResults, sendersFromTokenFoundInText);
        matchingSemanticResults.forEach(e -> e.setSender(sender));
        matchingSemanticResults.forEach(

                result -> {
                    boolean senderFound = result.getMetaData()
                                                .stream()
                                                .filter(e -> "sender".equals(e.getName()))
                                                .anyMatch(e -> sender.equals(e.getValue()));

                    if (senderFound) {
                        result.getMetaData().add(new Metadata("sender", sender));
                    }

                });
        LOG.debug(matchingSemanticResults);
        return matchingSemanticResults;
    }

    @NotNull
    private List<SenderToken> getSendersFromKnowledgebase(String text) {
        return knowledgeBase.getSenders().stream().parallel().filter(e -> e.match(text)).collect(Collectors.toList());
    }

    @NotNull
    private List<SemanticResult> getTopicsWithMetadata(String text) {
        return knowledgeBase.getTopics()
                            .stream()
                            .parallel()
                            .filter(e -> e.match(text))
                            .map(e -> new SemanticResult(e.getName(), e.getMetaData(text)))
                            .collect(Collectors.toList());
    }

    /**
     * If no topic is found we still want to get the metadata
     *
     * @param text The text to extract Metadata from
     * @return returns SemanticResult.OTHER with metadata
     */
    @NotNull
    private SemanticResult getMetadatasForTopicOther(String text) {
        return knowledgeBase.getTopics()
                            .stream()
                            .parallel()
                            .map(e -> new SemanticResult(Topic.OTHER, e.getMetaData(text)))
                            .collect(Collectors.toList())
                            .stream()
                            .filter(e -> e.getMetaData().size() > 0)
                            .findFirst()
                            .orElse(new SemanticResult(Topic.OTHER, new ArrayList<>(0)));
    }

    private String getSender(@NotNull final List<SemanticResult> foundTopics, @NotNull final List<SenderToken> senderTokens) {
        if (senderTokens.size() == 1) {
            return senderTokens.get(0).getName();
        }

        final List<Metadata> sendersFromTopic = new ArrayList<>();
        for (SemanticResult result : foundTopics) {
            sendersFromTopic.addAll(result.getMetaData().stream().filter(e -> SenderToken.SENDER.equals(e.getName()))
                                          .collect(Collectors.toList()));
        }

        return sendersFromTopic.stream()
                               .map(e -> new SenderToken(e.getValue()))
                               .filter(senderTokens::contains)
                               .findFirst()
                               .orElse(new SenderToken(SenderToken.UNKNOWN))
                               .getName();
    }

    public void manualParse(Topic parseTopic) {
        List<Topic> topics = new ArrayList<>(1);
        topics.add(parseTopic);
        knowledgeBase.setTopics(topics);
    }
}
