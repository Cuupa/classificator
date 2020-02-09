package com.cuupa.classificator.services.kb.semantic.dataExtraction;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class PhoneNumberExtract extends Extract {

    private final Pattern pattern;

    public PhoneNumberExtract(@NotNull String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String normalize(final String value) {
        return value;
    }
}
