package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.services.kb.semantic.Metadata;
import com.cuupa.classificator.services.kb.semantic.SenderToken;
import com.cuupa.classificator.services.kb.semantic.Topic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class KnowledgeBaseExecutorService {

    private static final Log LOG = LogFactory.getLog(KnowledgeBaseExecutorService.class);

    private ExecutorService executorServicePool = Executors.newCachedThreadPool();

    public List<SemanticResult> submit(final List<Topic> topics, final List<SenderToken> senderTokens, final String text) throws ExecutionException, InterruptedException {
        CompletableFuture<List<SemanticResult>> futureTopics = CompletableFuture.supplyAsync(() -> getTopics(topics,
                                                                                                             text),
                                                                                             executorServicePool);

        CompletableFuture<List<SenderToken>> futureSenders = CompletableFuture.supplyAsync(() -> getSenders(senderTokens,
                                                                                                            text),
                                                                                           executorServicePool);
        futureSenders.thenAcceptAsync(senderToken -> getNumberOfOccurences(senderTokens, text));

        final List<SenderToken> senders = futureSenders.get();

        String mostFittingSender = senders.stream()
                                          .max(Comparator.comparing(SenderToken::countNumberOfOccurences))
                                          .map(SenderToken::getName)
                                          .orElse(null);

        List<SemanticResult> semanticResults = futureTopics.get();

        if (semanticResults.isEmpty()) {
            LOG.debug("Found no matching Topics");
            SemanticResult other = getMetadatasForTopicOther(topics, text);
            semanticResults.add(other);
        }

        if (mostFittingSender == null) {
            final List<Metadata> sendersFromTopic = new ArrayList<>();
            for (SemanticResult result : semanticResults) {
                sendersFromTopic.addAll(result.getMetaData()
                                              .stream()
                                              .filter(e -> SenderToken.SENDER.equals(e.getName()))
                                              .collect(Collectors.toList()));
            }
            sendersFromTopic.addAll(senderTokens.stream()
                                                .map(e -> new Metadata("sender", e.getName()))
                                                .collect(Collectors.toList()));

            mostFittingSender = sendersFromTopic.stream()
                                                .map(Metadata::getName)
                                                .findFirst()
                                                .orElse(SenderToken.UNKNOWN);
        }

        // ToDo: Senders from metadata
        String finalMostFittingSender = mostFittingSender;
        semanticResults.forEach(result -> result.setSender(finalMostFittingSender));
        semanticResults.forEach(

                result -> {
                    boolean senderFound = result.getMetaData()
                                                .stream()
                                                .filter(e -> "sender".equals(e.getName()))
                                                .anyMatch(e -> finalMostFittingSender.equals(e.getValue()));

                    if (senderFound) {
                        result.getMetaData().add(new Metadata("sender", finalMostFittingSender));
                    }

                });

        LOG.debug(semanticResults);
        return semanticResults;
    }

    /**
     * If no topic is found we still want to get the metadata
     *
     * @param text The text to extract Metadata from
     * @return returns SemanticResult.OTHER with metadata
     */
    private SemanticResult getMetadatasForTopicOther(List<Topic> topics, String text) {
        return topics.stream()
                     .map(e -> new SemanticResult(Topic.OTHER, e.getMetaData(text)))
                     .collect(Collectors.toList())
                     .stream()
                     .filter(e -> e.getMetaData().size() > 0)
                     .findFirst()
                     .orElse(new SemanticResult(Topic.OTHER, new ArrayList<>(0)));
    }

    private List<SenderToken> getSenders(List<SenderToken> senders, String text) {
        return senders.stream().parallel().filter(e -> e.match(text)).collect(Collectors.toList());
    }

    private List<SenderToken> getNumberOfOccurences(List<SenderToken> senders, String text) {
        senders.forEach(senderToken -> senderToken.countNumberOfOccurences(text));
        return senders;
    }

    private List<SemanticResult> getTopics(List<Topic> topics, final String text) {
        return topics.stream()
                     .filter(e -> e.match(text))
                     .map(e -> new SemanticResult(e.getName(), e.getMetaData(text)))
                     .collect(Collectors.toList());
    }
}
