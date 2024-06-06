package ru.otus.spring.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import static java.lang.Boolean.TRUE;

@Data
@AllArgsConstructor
public class Answer {

    private String text;

    private Boolean correctAnswer;

    public String toString() {
        return (TRUE.equals(correctAnswer) ? "+" : "-") + " " + text;
    }
}
