package ru.otus.hw.service;


import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.formatters.AnswerFormatter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class TestServiceImplTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final AppProperties appProperties = mock(AppProperties.class);

    private final QuestionDao questionDao = new CsvQuestionDao(appProperties);

    private final AnswerFormatter answerFormatter = new AnswerFormatter();

    private final IOService ioService = new StreamsIOService(new PrintStream(outputStreamCaptor));

    private final TestServiceImpl testService = new TestServiceImpl(ioService, questionDao, answerFormatter);

    @Test
    void executeTest() {
        doReturn("fixture_questions.csv").when(appProperties).getTestFileName();
        testService.executeTest();
        var output = outputStreamCaptor.toString();
        assertNotNull(output);
        assertThat(output).contains("+ Absolutely not");
    }
}