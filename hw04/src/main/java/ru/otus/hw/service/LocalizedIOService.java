package ru.otus.hw.service;

import ru.otus.hw.formatters.Tag;

public interface LocalizedIOService extends LocalizedMessagesService, IOService {
    void printLineLocalized(String code);

    void printTagFormattedLine(Tag tag, String code, Object ...args);

    void printFormattedLineLocalized(String code, Object ...args);

    String readStringWithPromptLocalized(String promptCode);
}
