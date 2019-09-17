package com.cuupa.classificator.services.kb.semantic;

import com.cuupa.classificator.services.kb.semantic.token.Token;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
public class SenderToken {

    public static final String UNKNOWN = "UNKNOWN";

    public static final String SENDER = "sender";

    private final List<Token> tokenList = new ArrayList<>();

    private String senderName;

    public SenderToken(String senderName) {
        this.senderName = senderName;
    }

    public SenderToken() {
    }

    public void addToken(Token token) {
        tokenList.add(token);
    }

    public boolean match(String text) {
        for (Token token : tokenList) {
            if (!token.match(text)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return senderName;
    }

    public void setName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof SenderToken) {
            SenderToken senderTokenToCompare = (SenderToken) obj;
            if (senderTokenToCompare.getName() == null && senderName == null) {
                return true;
            }
            return senderTokenToCompare.getName().equals(senderName);
        }
        return false;
    }
}
