package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.services.kb.semantic.Topic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class KnowledgeManager {

    private static final Log LOG = LogFactory.getLog(KnowledgeManager.class);

    private KnowledgeBase knowledgeBase;

    private KnowledgeBaseInitiator knowledgeBaseInitiator;

    private KnowledgeBaseExecutorService knowledgeBaseExecutorService;

    public KnowledgeManager(KnowledgeBaseInitiator knowledgeBaseInitiator, KnowledgeBaseExecutorService knowledgeBaseExecutorService) {
        this.knowledgeBaseInitiator = knowledgeBaseInitiator;
        this.knowledgeBaseExecutorService = knowledgeBaseExecutorService;
        knowledgeBase = this.knowledgeBaseInitiator.initKnowledgeBase();
    }

    public void reloadKB() {
        knowledgeBase.clear();
        knowledgeBase = knowledgeBaseInitiator.initKnowledgeBase();
    }

    public List<SemanticResult> getResults(final String text) {
        try {
            return knowledgeBaseExecutorService.submit(knowledgeBase.getTopics(), knowledgeBase.getSenders(), text);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void manualParse(Topic parseTopic) {
        List<Topic> topics = new ArrayList<>(1);
        topics.add(parseTopic);
        knowledgeBase.setTopics(topics);
    }
}
