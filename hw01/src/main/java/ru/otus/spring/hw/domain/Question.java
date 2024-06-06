package ru.otus.spring.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Question {

    private String questionText;

    private List<Answer> answerList;
}
