package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value("#{T(java.lang.Integer).parseInt('${test.rightAnswersCountToPass}')}")
    private int rightAnswersCountToPass;

    @Value("${test.testFileName}")
    private String testFileName;
}
