package ru.otus.spring.hw.exceptions;

public class QuestionsLoadException extends RuntimeException {
    public QuestionsLoadException(String message, Throwable ex) {
        super(message, ex);
    }

    public QuestionsLoadException(String message) {
        super(message);
    }
}
