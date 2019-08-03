package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import java.util.regex.Pattern;

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
public class SenderExtract extends Extract {

    private static Pattern pattern;

    public SenderExtract(String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String normalize(String value) {
        return value;
    }
}
