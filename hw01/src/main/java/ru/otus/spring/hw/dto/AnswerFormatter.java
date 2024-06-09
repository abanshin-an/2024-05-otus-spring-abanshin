package ru.otus.spring.hw.dto;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw.domain.Answer;

@Component
public class AnswerFormatter {
    public String format(Answer answer) {
            return (answer.isCorrectAnswer() ? "+" : "-") + " " + answer.getText();
    }
}
