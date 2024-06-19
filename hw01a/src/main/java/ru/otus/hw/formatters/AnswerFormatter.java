package ru.otus.hw.formatters;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Answer;

@Component
public class AnswerFormatter {
    public String format(Answer answer) {
        return (answer.isCorrect() ? "+" : "-") + " " + answer.text();
    }
}
