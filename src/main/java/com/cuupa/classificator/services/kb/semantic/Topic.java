package com.cuupa.classificator.services.kb.semantic;

import com.cuupa.classificator.services.kb.semantic.token.MetaDataToken;
import com.cuupa.classificator.services.kb.semantic.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Topic {

    public static final String OTHER = "OTHER";

    private final List<Token> tokenList = new ArrayList<>();

    private String topicName;

    private List<Metadata> metadata = new ArrayList<>();

    @NotNull
    private List<MetaDataToken> metaDataToken = new ArrayList<>();

    public void addToken(Token token) {
        tokenList.add(token);
    }

    public String getName() {
        return topicName;
    }

    public void setName(String topicName) {
        this.topicName = topicName;
    }

    public boolean match(String text) {
        return tokenList.stream().allMatch(token -> token.match(text));
    }

    public void addMetaData(MetaDataToken metadata) {
        this.metaDataToken.add(metadata);
    }

    @NotNull
    public List<Metadata> getMetaData(final String text) {
        if (metadata.isEmpty()) {
            metaDataToken.stream().map(data -> data.extract(text)).forEach(metadata::addAll);
        }
        return metadata;
    }

    @NotNull
    public List<MetaDataToken> getMetaDataList() {
        return metaDataToken;
    }


    public void addMetaDataList(@NotNull List<MetaDataToken> metaDataTokenList) {
        metaDataToken.addAll(metaDataTokenList);
    }
}
