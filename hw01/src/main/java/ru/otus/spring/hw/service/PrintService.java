package ru.otus.spring.hw.service;

public interface PrintService {
    void printLine(String s);

    void printFormattedLine(String s, Object... args);
}
