package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.IoConfig;
import ru.otus.hw.exceptions.AnswerException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class StreamsIOService implements IOService {

    private final PrintStream printStream;

    private final Scanner scanner;

    private final IoConfig ioConfig;

    public StreamsIOService(@Value("#{T(System).out}") PrintStream printStream,
                            @Value("#{T(System).in}") InputStream inputStream,
                            IoConfig ioConfig) {

        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
        this.ioConfig = ioConfig;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return scanner.nextLine();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < ioConfig.getMaxAnswerAttemps(); i++) {
            try {
                var stringValue = scanner.nextLine();
                int intValue = Integer.parseInt(stringValue);
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                printLine(errorMessage);
            }
        }
        throw new AnswerException("Error during reading answer");
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        printLine(prompt);
        return readIntForRange(min, max, errorMessage);
    }
}
