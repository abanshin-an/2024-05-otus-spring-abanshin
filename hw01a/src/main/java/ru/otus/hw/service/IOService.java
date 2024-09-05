package ru.otus.hw.services;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object... args);
}
