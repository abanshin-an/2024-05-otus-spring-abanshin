package ru.otus.spring.hw.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AppProperties implements QuestionsFileNameProvider {

    private final String questionsFileName;
}
