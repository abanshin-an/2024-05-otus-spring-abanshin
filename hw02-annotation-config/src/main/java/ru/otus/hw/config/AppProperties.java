package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppProperties implements TestConfig, TestFileNameProvider, IoConfig {

    private final int rightAnswersCountToPass;

    private final String testFileName;

    private final int maxAnswerAttempts;

    public AppProperties(
            @Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass,
            @Value("${test.testFileName}") String testFileName,
            @Value("${io.maxAnswerAttempts}") int maxAnswerAttempts) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName;
        this.maxAnswerAttempts = maxAnswerAttempts;
    }
}
