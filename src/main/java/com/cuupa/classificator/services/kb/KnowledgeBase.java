package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.services.kb.semantic.SenderToken;
import com.cuupa.classificator.services.kb.semantic.Topic;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBase {

    private List<Topic> topicList;

    private List<SenderToken> senders;

    public void clear() {
        topicList.clear();
        senders.clear();
    }

    public List<SenderToken> getSenders() {
        return new ArrayList<>(senders);
    }

    public void setSenders(List<SenderToken> senders) {
        this.senders = senders;
    }

    public List<Topic> getTopics() {
        return new ArrayList<>(topicList);
    }

    public void setTopics(List<Topic> topicList) {
        this.topicList = topicList;
    }
}
