package com.cuupa.classificator.services.kb.semantic.token;

import com.cuupa.classificator.services.kb.semantic.text.TextSearch;
import org.jetbrains.annotations.NotNull;

public class CountToken extends Token {

    @Override
    public boolean match(String text) {
        return false;
    }

    @Override
    public int getDistance() {
        return 0;
    }

    @NotNull
    @Override
    protected Token clone() {
        return new CountToken();
    }

    public int countOccurences(String name, String text) {
        TextSearch textSearch = new TextSearch(text);
        return textSearch.countOccurence(text);
    }
}
