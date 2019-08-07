package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * @author Simon Thiel (https://github.com/cuupa)
 */
public class SenderExtract extends Extract {

    private static Pattern pattern;

    public SenderExtract(@NotNull String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @NotNull
    @Override
    public String normalize(@NotNull String value) {
        return value.trim();
    }
}
