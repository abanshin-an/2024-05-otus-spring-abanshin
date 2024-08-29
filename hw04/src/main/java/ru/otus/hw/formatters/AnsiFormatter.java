package ru.otus.hw.formatters;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class AnsiFormatter implements Formatter {

    private static final String ANSI_RED = "\u001B[31m";

    private static final String ANSI_GREEN = "\u001B[32m";

    private static final String ANSI_BOLD = "\u001B[1m";

    private static final String ANSI_UNDERLINE = "\u001B[4m";

    private static final String ANSI_RESET = "\u001B[0m";

    private static final Map<Tag, String> TAG_MAP;

    public String format(Tag tag, String text) {
        return getStringElement(tag) + text + ANSI_RESET;
    }

    private String getStringElement(Tag tag) {
        return TAG_MAP.get(tag);
    }

    static {
        TAG_MAP = new EnumMap<>(Tag.class);
        TAG_MAP.put(Tag.QUESTION, ANSI_BOLD);
        TAG_MAP.put(Tag.ANSWER_CORRECT, ANSI_GREEN);
        TAG_MAP.put(Tag.ANSWER_WRONG, ANSI_RED);
        TAG_MAP.put(Tag.TITLE, ANSI_BOLD + ANSI_UNDERLINE);
    }
}
