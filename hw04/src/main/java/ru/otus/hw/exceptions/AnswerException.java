package ru.otus.hw.exceptions;

public class AnswerException extends RuntimeException {

    public AnswerException(String message, Throwable ex) {
        super(message, ex);
    }

    public AnswerException(String message) {
        super(message);
    }

}