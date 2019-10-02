package com.cuupa.classificator.services.kb;

import com.cuupa.classificator.services.kb.semantic.SenderToken;
import com.cuupa.classificator.services.kb.semantic.Topic;

import java.util.ArrayList;
import java.util.List;

class KnowledgeBase {

    private List<Topic> topicList;

    private List<SenderToken> senders;

    void clear() {
        topicList.clear();
        senders.clear();
    }

    List<SenderToken> getSenders() {
        return new ArrayList<>(senders);
    }

    void setSenders(List<SenderToken> senders) {
        this.senders = senders;
    }

    List<Topic> getTopics() {
        return new ArrayList<>(topicList);
    }

    void setTopics(List<Topic> topicList) {
        this.topicList = topicList;
    }
}
