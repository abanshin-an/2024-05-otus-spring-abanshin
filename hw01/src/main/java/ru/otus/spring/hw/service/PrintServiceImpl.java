package ru.otus.spring.hw.service;

import java.io.PrintStream;

public class PrintServiceImpl implements PrintService {
    private final PrintStream printStream;

    public PrintServiceImpl(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }
}
